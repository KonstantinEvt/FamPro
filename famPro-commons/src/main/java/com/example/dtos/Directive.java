package com.example.dtos;

import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class Directive implements Serializable {
    private String tokenUser;
    private String person;
    private SwitchPosition switchPosition;
    private KafkaOperation operation;
}
