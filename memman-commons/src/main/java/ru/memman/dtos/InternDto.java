package ru.memman.dtos;

import ru.memman.enums.Assignment;
import ru.memman.enums.Localisation;
import ru.memman.enums.SecretLevel;
import ru.memman.enums.WorkStatus;
import lombok.*;


@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class InternDto {
    private Long id;
    private String description;
    private Assignment assignment;
    private WorkStatus workStatus;
    private SecretLevel secretLevel;
    private Localisation localisation;
}
