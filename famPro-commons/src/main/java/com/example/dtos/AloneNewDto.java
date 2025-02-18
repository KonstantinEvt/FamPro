package com.example.dtos;

import com.example.enums.NewsCategory;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AloneNewDto {
    private UUID id;
    private String sendingFrom;
    private String sendingTo;
    private Date creationDate;
    private String subject;
    private byte[] image;
    private String textInfo;
    private NewsCategory category;
    private boolean alreadyRead;
}
