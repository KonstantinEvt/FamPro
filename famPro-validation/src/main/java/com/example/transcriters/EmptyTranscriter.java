package com.example.transcriters;


import com.example.enums.CheckStatus;

public class EmptyTranscriter implements AbstractTranscriter {

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
    public String getBirth() {
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


}

