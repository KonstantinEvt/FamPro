package com.example.dtos;

import com.example.enums.CheckStatus;
import com.example.enums.Localisation;
import com.example.enums.Subject;
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
        Subject subject;
        Localisation localisation;
    }


