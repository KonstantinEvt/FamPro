package com.family.fampro.dto;

import com.family.fampro.entity.FamilyMember;
import lombok.Data;

import java.sql.Date;
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
