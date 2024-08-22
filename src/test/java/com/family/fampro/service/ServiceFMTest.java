package com.family.fampro.service;

import com.family.fampro.entity.FamilyMember;
import com.family.fampro.mapper.FamilyMemberMapper;
import com.family.fampro.repository.FamilyRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceFMTest {
    @Mock
    FamilyRepo familyRepo;
    @Mock
    FamilyMemberMapper familyMemberMapper;

    @InjectMocks
    ServiceFM serviceFM;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getFamilyMemberTest() {
        FamilyMember testMember=FamilyMember.builder()
                .firstname("unknown")
                .lastname("unknown")
                .middlename("unknown").build();
        FamilyMember mockFM=mock(FamilyMember.class);
        when(familyRepo.findById(1L)).thenReturn(Optional.of(mockFM));
        assertEquals(testMember, serviceFM.getFamilyMember(1L));
    }
}