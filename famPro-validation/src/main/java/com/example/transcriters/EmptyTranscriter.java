package com.example.transcriters;


import com.example.enums.CheckStatus;

import java.util.HashMap;
import java.util.Map;

public class EmptyTranscriter implements AbstractTranscriter {
    public static final Map<String,String> matrixOfChange=new HashMap<>();

    @Override
    public String transcritWordToLocalisation(String str) {
        return str;
    }

    @Override
    public String transcritWordFromLocalisation(String str) {
        return str;
    }

    @Override
    public String getAbsent() {
        return CheckStatus.ABSENT.getComment();
    }

    @Override
    public String getInfoNotFully() {
        return CheckStatus.NOT_FULLY.getComment();
    }

    @Override
    public String getIncorrectInfo() {
        return CheckStatus.UNCORRECTED.getComment();
    }

    @Override
    public String getBirthdayString() {
        return " . Birthday: ";
    }

    @Override
    public String getOut() {
        return "Information is absent";
    }

    @Override
    public String empty() {
        return "<empty>";
    }

    @Override
    public Map<String, String> getMatrixOfChange() {
        return matrixOfChange;
    }


}

