package ru.memman.transcriters;


import ru.memman.enums.CheckStatus;
import ru.memman.enums.Relation;
import ru.memman.enums.Subject;
import ru.memman.enums.SwitchPosition;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class EnglishTranscripter implements AbstractTranscripter {

    @Override
    public String transcritWordToLocalisation(String str) {
        return str;
    }

    @Override
    public String transcritWordFromLocalisation(String str) {
        return str;
    }

    @Override
    public String getSpecify(Relation relation) {
        return relation.getCommit();
    }

    @Override
    public String getAbsent() {
        return "Anonymous";
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
    public String getTextSubject(Subject subject) {
        return subject.getCommit();
    }

    @Override
    public String getTextSwitchPosition(SwitchPosition switchPosition) {
        return switchPosition.getCommit();
    }

    @Override
    public String getAnd() {
        return " and ";
    }

    @Override
    public String getRelation(Relation relation) {
        return relation.getCommit();
    }

    @Override
    public String getDefaultInfo() {
        return "Contact description is must here";
    }
}

