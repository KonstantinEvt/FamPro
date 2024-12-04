package com.example.dtos;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class PhoneDto extends InternDto{
    private String internName;
}
