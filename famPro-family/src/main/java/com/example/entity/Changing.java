package com.example.entity;

import com.example.enums.ChangingStatus;
import com.example.enums.KafkaOperation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Changing {
    private boolean changeIsPresent;
    private boolean changingMain;
    private ChangingStatus changingFather;
    private ChangingStatus changingMother;
    private boolean oneChildInFamily;

    public Changing() {
        this.changeIsPresent = false;
        this.oneChildInFamily = false;
        this.changingMain = false;
        this.changingFather = ChangingStatus.NONE;
        this.changingMother = ChangingStatus.NONE;
    }
}
