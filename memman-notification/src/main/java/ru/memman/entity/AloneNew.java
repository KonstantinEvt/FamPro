package ru.memman.entity;

import ru.memman.enums.Attention;
import ru.memman.enums.Localisation;
import ru.memman.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AloneNew {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "extern_id")
    private String externId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "sending_from")
    private Recipient sendFrom;

    @Column(name = "exist_prime")
    private boolean existPrimePhoto;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "letter_recipient",
            joinColumns = @JoinColumn(name = "letter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "man_id", referencedColumnName = "id"))
    private Set<Recipient> sendTo;

    @OneToMany(mappedBy = "mainLetter", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<LetterLocal> letterLocalisations;

    @OneToMany(mappedBy = "letter", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Voting> voting;

    @Enumerated(EnumType.STRING)
    @Column(name = "attention")
    private Attention attention;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "subject")
    private String subject;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "text_info", length = 2043)
    private String textInfo;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NewsCategory category;

    @Column(name = "notReadingString")
    private String notReadingString;

    @Column(name = "already_read")
    private boolean alreadyRead;

    @Column(name = "exist_loc")
    private boolean existLocalisations;

    @Enumerated(EnumType.STRING)
    private Localisation localisation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AloneNew aloneNew = (AloneNew) o;
        return existPrimePhoto == aloneNew.existPrimePhoto && existLocalisations == aloneNew.existLocalisations && Objects.equals(id, aloneNew.id) && Objects.equals(externId, aloneNew.externId) && Objects.equals(sendFrom, aloneNew.sendFrom) && Objects.equals(sendTo, aloneNew.sendTo) && Objects.equals(letterLocalisations, aloneNew.letterLocalisations) && attention == aloneNew.attention && Objects.equals(creationDate, aloneNew.creationDate) && Objects.equals(subject, aloneNew.subject) && Objects.equals(imageUrl, aloneNew.imageUrl) && Objects.equals(textInfo, aloneNew.textInfo) && category == aloneNew.category && Objects.equals(notReadingString, aloneNew.notReadingString) && localisation == aloneNew.localisation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

