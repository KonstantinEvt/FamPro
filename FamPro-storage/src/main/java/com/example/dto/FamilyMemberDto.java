package com.example.dto;

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
    private Long mother_id;
    private Long father_id;
}
