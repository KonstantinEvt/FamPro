package ru.memman.dtos;

import ru.memman.enums.CheckStatus;
import ru.memman.enums.Localisation;
import ru.memman.enums.Subject;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class DirectiveGuards extends Directive {
        private String id;
        private Set<String> guards;
        private long number1;
        private int number2;
        private CheckStatus checkStatus;
        private String info1;
        private String info2;
        private String info3;
        private Timestamp created;
        private boolean photoExist;
        private Subject subject;
        private Localisation localisation;
    }


