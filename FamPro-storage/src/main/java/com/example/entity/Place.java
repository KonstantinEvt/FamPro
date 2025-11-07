package com.example.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@SuperBuilder
public class Place extends InternEntity {
    /**
     * Страна
     */
    @Column(name = "country", length = 50)
    private String country;

    /**
     * Регион или область
     */
    @Column(name = "region", length = 50)
    private String region;

    /**
     * Город
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * Улица
     */
    @Column(name = "street", length = 50)
    private String street;

    @Column(name = "photo_exist")
    private boolean photoExist;
}
