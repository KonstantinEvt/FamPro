package ru.memman.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.memman.enums.Localisation;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class FamilyDirective extends Directive {
    private FamilyMemberDto familyMemberDto;
    private Localisation localisation;
}
