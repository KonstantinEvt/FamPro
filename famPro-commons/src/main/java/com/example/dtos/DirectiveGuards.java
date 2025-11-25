package com.example.dtos;

import com.example.enums.CheckStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class DirectiveGuards extends Directive {
        private String id;
        private Set<String> guards;
        private int globalNumber1;
        private int globalNumber2;
        private CheckStatus checkStatus;
        private String info1;
        private String info2;
        private String info3;
        private Timestamp created;
    }


