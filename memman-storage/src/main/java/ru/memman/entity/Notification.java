package ru.memman.entity;

import ru.memman.enums.Attention;
import ru.memman.enums.Localisation;
import ru.memman.enums.Subject;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Notification {
    private String token;
    private String person;
    private Long id;
    private Attention attention;
    private Subject subject;
    private Localisation localisation;
}
