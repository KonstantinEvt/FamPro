package com.example.transcriters;


import com.example.enums.Attention;
import com.example.enums.CheckStatus;
import com.example.enums.Subject;
import com.example.enums.SwitchPosition;

import java.util.HashMap;
import java.util.Map;

public class EnglishTranscripter implements AbstractTranscripter {
    public static final Map<String,String> matrixOfChange=new HashMap<>();
    public final Map<String,String> matrixOfTextGeneration= Map.ofEntries(

    );

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
    public String getClose() {
        return "Hidden";
    }

    @Override
    public String empty() {
        return "<empty>";
    }

    @Override
    public Map<String, String> getMatrixOfChange() {
        return matrixOfChange;
    }

    @Override
    public String getTextSubject(Subject subject) {
        return subject.getCommit();
    }

    @Override
    public Map<String, String> getMatrixOfTextGeneration() {
        return matrixOfTextGeneration;
    }
    @Override
    public String getTextSwitchPosition(SwitchPosition switchPosition) {
        return switchPosition.getCommit();
    }

    @Override
    public String getAnd() {
        return " and ";
    }
}

