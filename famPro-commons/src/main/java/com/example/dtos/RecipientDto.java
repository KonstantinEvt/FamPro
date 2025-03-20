package com.example.dtos;

import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class RecipientDto {
        private Long id;
        private String ExternId;
        private String info;
        private String name;
        private String urlPhoto;
    }
