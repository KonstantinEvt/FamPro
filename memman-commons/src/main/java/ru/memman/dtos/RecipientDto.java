package ru.memman.dtos;

import ru.memman.enums.Localisation;
import lombok.*;

import java.sql.Timestamp;

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
        private Timestamp lastOnline;
    }
