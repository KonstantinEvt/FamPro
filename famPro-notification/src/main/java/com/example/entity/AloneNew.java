package com.example.entity;

import com.example.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AloneNew {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "sending_from")
    private Recipient sendFrom;
    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "letter_recipient",
            joinColumns = @JoinColumn(name = "letter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "man_id", referencedColumnName = "id"))
    private Set<Recipient> sendTo;
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
}
