package com.example.entity;

import com.example.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AloneNew {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageGen")
    @SequenceGenerator(name = "messageGen",
            sequenceName = "seq_message_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;
    @Column(name = "from")
    private String sendingFrom;
    @Column(name = "to")
    private String sendingTo;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "subject")
    private String subject;
    @Column(name = "image")
    private String image;
    @Column(name = "textInfo")
    private String textInfo;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NewsCategory category;
    @Column(name = "alreadyRead")
    private boolean alreadyRead;
}
