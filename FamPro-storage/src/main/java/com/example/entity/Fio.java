package com.example.entity;

import com.example.enums.Sex;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.UUID;

@MappedSuperclass
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Fio {
    //    @Id
//    private Long id;
    @Column(name = "first_name", length = 20)
    private String firstName;
    @Column(name = "middle_name", length = 50)
    private String middleName;
    @Column(name = "last_name", length = 50)
    private String lastName;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "UUID", unique = true)
    private UUID uuid;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
    @Column(name = "full_name")
    private String fullName;


}
