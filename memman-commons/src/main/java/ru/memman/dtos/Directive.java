package ru.memman.dtos;

import ru.memman.enums.KafkaOperation;
import ru.memman.enums.SwitchPosition;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@SuperBuilder
public class Directive implements Serializable {
    private String tokenUser;
    private String person;
    private SwitchPosition switchPosition;
    private KafkaOperation operation;
}
