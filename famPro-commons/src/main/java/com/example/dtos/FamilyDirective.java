package com.example.dtos;

import com.example.enums.KafkaOperation;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class FamilyDirective extends Directive {
    private FamilyMemberDto familyMemberDto;
}
