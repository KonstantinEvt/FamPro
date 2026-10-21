package ru.memman.dtos;

import lombok.*;
import ru.memman.enums.Localisation;
import ru.memman.enums.SecretLevel;
import ru.memman.enums.UserRole;

import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class OnlineUserDto implements Serializable {
    private int[] newsCounts;
    private String logName;
    private String fullName;
    private int onlinePeopleCount;
    private long peopleInBase;
    private long personsInBase;
    private String externUuid;
    private String nickName;
    private String email;
    private boolean primePhoto;
    private UserRole priorityRole;
    private String linkExternId;
    private Localisation localisation;
    private Timestamp lastOnline;
    private boolean online;
    private Timestamp lastUpdate;
    private SecretLevel secretLevelPhoto;
    private SecretLevel secretLevelPhone;
    private SecretLevel secretLevelEmail;
    private String mainPhone;
    private String mainEmail;
    private boolean sendOver;
}
