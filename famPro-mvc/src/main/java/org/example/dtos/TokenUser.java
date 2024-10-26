package org.example.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class TokenUser {
    private String username;
    private String password;
    private Map<String,Object> claims;
    private String role;
}
