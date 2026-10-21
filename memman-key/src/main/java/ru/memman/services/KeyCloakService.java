package ru.memman.services;

import lombok.extern.log4j.Log4j2;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.Localisation;
import ru.memman.enums.UserRole;
import ru.memman.exceptions.KeyCloakUserNotFound;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
@Log4j2
public class KeyCloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.default-role}")
    private String defaultRole;
    private final LinkedList<OnlineUserDto> inline;

    public KeyCloakService(Keycloak keycloak, LinkedList<OnlineUserDto> inline) {
        this.keycloak = keycloak;
        this.inline = inline;
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
        String userName;
        OnlineUserDto onlineUserDto = new OnlineUserDto();
        if (SecurityContextHolder.getContext().getAuthentication() == null) userName = dto.getUsername();
        else userName = getTokenUser().getUsername();
        onlineUserDto.setLogName(userName);
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
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
            onlineUserDto.setEmail(dto.getEmail());
        }
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) user.setLastName(dto.getLastName());
        if (user.getAttributes() == null) {
            user.setAttributes(new HashMap<>());
        }
        if (dto.getMiddleName() != null && !dto.getMiddleName().isBlank())
            user.getAttributes().put("middleName", Collections.singletonList(dto.getMiddleName()));
        if (dto.getBirthday() != null && !dto.getBirthday().isBlank())
            user.getAttributes().put("birthday", Collections.singletonList(dto.getBirthday()));
        if (dto.getNickName() != null && !dto.getNickName().isBlank()) {
            user.getAttributes().put("nickName", Collections.singletonList(dto.getNickName()));
            onlineUserDto.setNickName(dto.getNickName());
        }
        if (dto.getRoles() != null && dto.getRoles().contains(UserRole.LINKED_USER.getNameSSO())) {
            if (user.getRealmRoles() != null) {
                user.getRealmRoles().add(UserRole.LINKED_USER.getNameSSO());
            } else user.setRealmRoles(Collections.singletonList(UserRole.LINKED_USER.getNameSSO()));
        }
        if (user.getRealmRoles() != null) onlineUserDto.setPriorityRole(getPriorityRole(user.getRealmRoles()));
        UserResource userResource = usersResource.get(user.getId());
        userResource.update(user);
        addRolesToUser(user);
        onlineUserDto.setSendOver(true);
        inline.add(onlineUserDto);
    }

    public void chooseLocalisation(String loc) {
        String userName = getTokenUser().getUsername();
        UsersResource usersResource = getUsersResource();
        if (usersResource == null) log.info("User Resource not found");
        else {
            log.info("List of users is loaded");
            List<UserRepresentation> users = usersResource.search(userName);
//            if (users==null||users.isEmpty()) log.info("but user {} not found", userName); else {System.out.println(users.get(0).getUsername());}
//            UserRepresentation user = users.stream().filter(x -> x.getUsername().equals(userName)).findFirst().orElseThrow(() -> new KeyCloakUserNotFound("User not found"));
            UserRepresentation user = users.get(0);
            if (user.getAttributes() == null) {
                log.info("user attributes is not set yet");
                user.setAttributes(new HashMap<>());
            }
            if (user.getAttributes().get("locale") == null) {
                user.getAttributes().put("locale", Collections.singletonList(loc));
                UserResource userResource = usersResource.get(user.getId());
                userResource.update(user);
            } else {
                String locBase = user.getAttributes().get("locale").get(0);
                if (locBase == null || !Objects.equals(locBase, loc)) {
                    user.getAttributes().put("locale", Collections.singletonList(loc));
                    UserResource userResource = usersResource.get(user.getId());
                    userResource.update(user);
                    inline.add(OnlineUserDto.builder()
                            .externUuid((String) getTokenUser().getClaims().get("sub"))
                            .localisation(getLocalisationFromString(loc))
                            .online(true)
                            .sendOver(true)
                            .build());
                }
            }
        }
    }

    public Localisation getLocalisationFromString(String localisation) {
        if (localisation == null) return Localisation.RU;
        for (Localisation loc :
                Localisation.values()) {
            if (localisation.toUpperCase().equals(loc.name())) return loc;
        }
        return Localisation.RU;
    }

    private void addRolesToUser(UserRepresentation user) {
        Set<String> roles = new HashSet<>();
        if (user.getRealmRoles() == null) user.setRealmRoles(new ArrayList<>());
        if (!user.getRealmRoles().contains(UserRole.SIMPLE_USER.getNameSSO()))
            roles.add(UserRole.SIMPLE_USER.getNameSSO());
        if (!user.getRealmRoles().contains(UserRole.BASE_USER.getNameSSO())
                && user.getFirstName() != null
                && user.getLastName() != null
                && user.getAttributes().get("middleName") != null
                && user.getAttributes().get("birthday") != null) {
            roles.add(UserRole.BASE_USER.getNameSSO());
        }
        if (user.getRealmRoles().contains(UserRole.LINKED_USER.getNameSSO()))
            roles.add(UserRole.LINKED_USER.getNameSSO());
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

    private UserRole getPriorityRole(List<String> realmRoles) {
        for (UserRole role :
                UserRole.values()) {
            if (realmRoles.contains(role.getNameSSO())) return role;
        }
        return UserRole.SIMPLE_USER;
    }
}

