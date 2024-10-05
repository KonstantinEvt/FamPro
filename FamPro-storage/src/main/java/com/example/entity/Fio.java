package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "family_members")
public class Fio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqFio")
    @SequenceGenerator(
            name = "genSeqFio",
            sequenceName = "FioSeq", initialValue = 1, allocationSize = 5)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date Birthday;

}
