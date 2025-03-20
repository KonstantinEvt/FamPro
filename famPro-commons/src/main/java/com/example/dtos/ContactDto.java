package com.example.dtos;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ContactDto {
    private UUID uuid;
    private String ownerId;
    private String externId;
    private String name;
    private String info;
    private boolean contactPhoto;
    private boolean primePhoto;
}