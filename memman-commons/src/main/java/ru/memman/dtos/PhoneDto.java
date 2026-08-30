package ru.memman.dtos;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PhoneDto extends InternDto{
    private String internName;
}
