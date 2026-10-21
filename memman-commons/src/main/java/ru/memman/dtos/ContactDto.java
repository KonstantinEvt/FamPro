package ru.memman.dtos;

import lombok.*;
import ru.memman.enums.Localisation;
import ru.memman.enums.Relation;
import ru.memman.enums.SecretLevel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ContactDto implements Serializable {
    private UUID uuid;
    private String ownerId;
    private String linkPersonUuid;
    private String personUuid;
    private String name;
    private String info;
    private int relation;
    private String specify;
    private int secretLevelPhoto;
    private boolean contactPhoto;
    private boolean preferContact;
    private boolean primePhoto;
    private boolean change;
    private Localisation localisation;
    private Localisation personLocalisation;
    private boolean online;
    private Timestamp lastOnline;
    private int secretLevelPhone;
    private int secretLevelEmail;
    private String email;
    private String phone;

}