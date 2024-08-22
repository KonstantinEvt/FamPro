package com.family.fampro.proection;

import java.time.LocalDateTime;

public interface FamilyMemberWithoutParents {
    Long getId(Long id);

    String getFirstname(String firstname);

    String getLastName(String lastname);

    String getMiddlename(String middlename);

    LocalDateTime getBirthday(LocalDateTime birthday);
}
