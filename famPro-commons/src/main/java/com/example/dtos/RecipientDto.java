package com.example.dtos;

import com.example.enums.Localisation;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class RecipientDto {
        private Long id;
        private String externId;
        private String linkExternId;
        private String info;
        private String name;
        private Boolean urlPhoto;
        private Localisation localisation;
    }
