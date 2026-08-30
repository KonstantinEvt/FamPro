package ru.memman.service;

import ru.memman.mappers.FamilyMemberMapper;
import ru.memman.repository.FamilyMemberRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;

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