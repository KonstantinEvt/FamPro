package com.family.fampro.dto;

import com.family.fampro.entity.FamilyMember;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class FamilyMemberDto  {
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private Boolean sex;
    private Date birthday;
    private FamilyMember mother;
    private FamilyMember father;
}
