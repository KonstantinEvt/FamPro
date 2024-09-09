package com.example.dtos;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailDto extends InternDto{
    private String internName;
}
