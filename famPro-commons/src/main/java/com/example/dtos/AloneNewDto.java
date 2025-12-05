package com.example.dtos;

import com.example.enums.Attention;
import com.example.enums.Localisation;
import com.example.enums.NewsCategory;
import lombok.*;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AloneNewDto {
    private UUID id;
    private String externId;
    private String sendingFrom;
    private String sendingFromAlt;
    private String sendingTo;
    private Attention attention;
    private Date creationDate;
    private String subject;
    private String textInfo;
    private NewsCategory category;
    private boolean alreadyRead;
    private Localisation localisation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AloneNewDto that = (AloneNewDto) o;
        return Objects.equals(id, that.id) && Objects.equals(subject, that.subject) && Objects.equals(textInfo, that.textInfo) && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
