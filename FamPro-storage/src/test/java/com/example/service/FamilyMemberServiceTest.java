package com.example.service;

import com.example.entity.FamilyMember;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.FamilyMemberRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FamilyMemberServiceTest {
    @Mock
    FamilyMemberRepo familyMemberRepo;
    @Mock
    FamilyMemberMapper familyMemberMapper;

    @InjectMocks
    FamilyMemberService familyMemberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getFamilyMemberTest() {

//        FamilyMember mockFM=mock(FamilyMember.class);
//        when(familyMemberRepo.findById(1L)).thenReturn(Optional.of(mockFM));
    }
}