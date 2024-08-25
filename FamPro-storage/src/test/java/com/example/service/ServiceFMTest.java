package com.example.service;

import com.example.entity.FamilyMember;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.FamilyRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

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
        Assertions.assertEquals(testMember, serviceFM.getFamilyMember(1L));
    }
}