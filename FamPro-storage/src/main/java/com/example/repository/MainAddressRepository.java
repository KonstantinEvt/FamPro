package com.example.repository;

import com.example.entity.Address;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Repository

@Log4j2
public class MainAddressRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    public MainAddressRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Set<Address> findAddressesOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".addresses a left join ", base, ".addresses_of_family_member b on a.id=b.address_id where b.member_info_id=", id, ' ');
        try {
            return new HashSet<Address>(entityManager.createNativeQuery(request, Address.class).getResultList());
        } catch (RuntimeException e) {
            log.warn("Addresses in base not found: ", e);
        }
        return null;
    }
}
