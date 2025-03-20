package com.example.repository;

import com.example.entity.DeferredDirective;
import com.example.entity.ShortFamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectiveRepo extends JpaRepository<DeferredDirective, UUID> {
DeferredDirective findFirstByDirectiveMember(ShortFamilyMember member);
DeferredDirective findFirstByShortFamilyMemberLink(ShortFamilyMember member);
}
