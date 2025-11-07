package com.example.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Changing {
private boolean changingMain;
private boolean changingFather;
private boolean changingMother;
public Changing(){
    this.changingMain=false;
    this.changingFather=false;
    this.changingMother=false;
}
}
