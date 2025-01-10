package com.example.services;

import com.example.dtos.TokenUser;
import com.example.enums.UserRoles;
import com.example.exceptions.KeyCloakUserNotFound;
import lombok.Data;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data

public class KeyCloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.default-role}")
    private String defaultRole;
    WebClient webClient;

    public KeyCloakService(Keycloak keycloak, WebClient webClient) {
        this.keycloak = keycloak;
        this.webClient = webClient;
    }

    public TokenUser getTokenUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClaims(jwt.getClaims());
        tokenUser.setUsername((String) jwt.getClaims().get("preferred_username"));
        Set<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        tokenUser.setRoles(roles);
        return tokenUser;
    }

    //addUser must be option set
    public void addUser(TokenUser dto) {
        String username = dto.getUsername();
        CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        List<UserRepresentation> users = usersResource.search(user.getUsername());
        UserRepresentation userAdded = users.stream().filter(x -> x.getUsername().equals(username)).findFirst().orElseThrow(() -> new KeyCloakUserNotFound("User not found"));
        addRolesToUser(userAdded);
    }

    public void editUser(TokenUser dto) {
        String userName = getTokenUser().getUsername();
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(userName);
        UserRepresentation user = users.stream().filter(x -> x.getUsername().equals(userName)).findFirst().orElseThrow(() -> new KeyCloakUserNotFound("User not found"));
        // Изменение username можно включить при добавлении выхода из сессии пользователя
//        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
            user.setCredentials(Collections.singletonList(credential));
        }
        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) user.setFirstName(dto.getFirstName());
        //Разобраться с Identity провадером OpenID и настройками для изменения Email
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) user.setEmail(dto.getEmail());
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) user.setLastName(dto.getLastName());
        if (user.getAttributes() == null) {
            user.setAttributes(new HashMap<>());
        }
        if (dto.getMiddleName() != null && !dto.getMiddleName().isBlank())
            user.getAttributes().put("middleName", Collections.singletonList(dto.getMiddleName()));
        if (dto.getBirthday() != null && !dto.getBirthday().isBlank())
            user.getAttributes().put("birthday", Collections.singletonList(dto.getBirthday()));
        if (dto.getNickName() != null && !dto.getNickName().isBlank())
            user.getAttributes().put("nickName", Collections.singletonList(dto.getNickName()));
        if (dto.getRoles() != null && dto.getRoles().contains(UserRoles.LINKED_USER.getNameSSO())) {
            if (user.getRealmRoles() != null) {
                user.getRealmRoles().add(UserRoles.LINKED_USER.getNameSSO());
            } else user.setRealmRoles(Collections.singletonList(UserRoles.LINKED_USER.getNameSSO()));
        }
        UserResource userResource = usersResource.get(user.getId());
        userResource.update(user);
        addRolesToUser(user);
    }

    public void chooseLocalisation(String loc) {
        String userName = getTokenUser().getUsername();
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(userName);
        UserRepresentation user = users.stream().filter(x -> x.getUsername().equals(userName)).findFirst().orElseThrow(() -> new KeyCloakUserNotFound("User not found"));
        if (user.getAttributes() == null) user.setAttributes(new HashMap<>());
        user.getAttributes().put("localisation", Collections.singletonList(loc));
        UserResource userResource = usersResource.get(user.getId());
        userResource.update(user);
    }

    private void addRolesToUser(UserRepresentation user) {
        Set<String> roles = new HashSet<>();
        if (user.getRealmRoles() == null) user.setRealmRoles(new ArrayList<>());
        if (!user.getRealmRoles().contains(UserRoles.SIMPLE_USER.getNameSSO()))
            roles.add(UserRoles.SIMPLE_USER.getNameSSO());
        if (!user.getRealmRoles().contains(UserRoles.BASE_USER.getNameSSO())
                && user.getFirstName() != null
                && user.getLastName() != null
                && user.getAttributes().get("middleName") != null
                && user.getAttributes().get("birthday") != null) {
            roles.add(UserRoles.BASE_USER.getNameSSO());
        }
        if (user.getRealmRoles().contains(UserRoles.LINKED_USER.getNameSSO()))
            roles.add(UserRoles.LINKED_USER.getNameSSO());
        if (!roles.isEmpty()) addRealmRoleToUser(user.getId(), roles);
    }

    /**
     * Добавление роли из существующих в KeyCloak
     *
     * @param userId - кому роль
     * @param roles  - сет новых ролей
     */
    private void addRealmRoleToUser(String userId, Set<String> roles) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        for (String newRole :
                roles) {
            RoleRepresentation role = realmResource.roles().get(newRole).toRepresentation();
            RoleMappingResource roleMappingResource = userResource.roles();
            roleMappingResource.realmLevel().add(Collections.singletonList(role));
        }
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}

