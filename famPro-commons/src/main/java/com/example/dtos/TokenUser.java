package com.example.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TokenUser implements Serializable {
    private String username;
    private String password;
    private Map<String,Object> claims;
    private String role;
}
