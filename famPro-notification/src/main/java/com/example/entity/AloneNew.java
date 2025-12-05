package com.example.entity;

import com.example.enums.Attention;
import com.example.enums.CheckStatus;
import com.example.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "letter_recipient",
            joinColumns = @JoinColumn(name = "letter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "man_id", referencedColumnName = "id"))
    private Set<Recipient> sendTo;

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

    @Column(name = "already_read")
    private boolean alreadyRead;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AloneNew aloneNew = (AloneNew) o;
        return alreadyRead == aloneNew.alreadyRead && Objects.equals(id, aloneNew.id) && Objects.equals(sendFrom, aloneNew.sendFrom) && Objects.equals(sendTo, aloneNew.sendTo) && Objects.equals(creationDate, aloneNew.creationDate) && Objects.equals(subject, aloneNew.subject) && Objects.equals(imageUrl, aloneNew.imageUrl) && Objects.equals(textInfo, aloneNew.textInfo) && category == aloneNew.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
