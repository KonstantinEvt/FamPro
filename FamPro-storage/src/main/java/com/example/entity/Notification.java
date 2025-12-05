package com.example.entity;

import com.example.enums.Attention;
import com.example.enums.Localisation;
import com.example.enums.Subject;
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
