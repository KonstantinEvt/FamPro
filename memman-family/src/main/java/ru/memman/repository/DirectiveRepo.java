package ru.memman.repository;

import ru.memman.entity.DeferredDirective;
import ru.memman.entity.ShortFamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectiveRepo extends JpaRepository<DeferredDirective, UUID> {
//DeferredDirective findFirstByDirectiveMember(ShortFamilyMember member);
//DeferredDirective findFirstByShortFamilyMemberLink(ShortFamilyMember member);
}
