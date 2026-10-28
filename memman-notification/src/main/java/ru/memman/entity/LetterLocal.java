package ru.memman.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.memman.enums.Localisation;

import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class LetterLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_letter")
    private AloneNew mainLetter;

    @Enumerated(EnumType.STRING)
    private Localisation localisation;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text_info", length = 2043)
    private String textInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetterLocal that = (LetterLocal) o;
        return Objects.equals(id, that.id) && Objects.equals(mainLetter, that.mainLetter) && localisation == that.localisation && Objects.equals(subject, that.subject) && Objects.equals(textInfo, that.textInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
