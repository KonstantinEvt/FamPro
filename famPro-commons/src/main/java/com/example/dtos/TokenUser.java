package com.example.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TokenUser implements Serializable {
    private String username;
    private String password;
    private String nickName;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthday;
    private Map<String,Object> claims;
    private Set<String> roles;
    private String localisation;
}
