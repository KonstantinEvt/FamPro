package ru.memman.transcriters;

import ru.memman.enums.Relation;
import ru.memman.enums.Subject;
import ru.memman.enums.SwitchPosition;

import java.util.Map;

public interface AbstractTranscripter {
String transcritWordToLocalisation(String str);
String transcritWordFromLocalisation(String str);
String getSpecify(Relation relation);
String getAbsent();
String getInfoNotFully();
String getIncorrectInfo();
String getBirthdayString();
String getOut();
String getClose();
String empty();
String getTextSubject(Subject subject);
String getTextSwitchPosition(SwitchPosition switchPosition);
String getAnd();
String getRelation(Relation relation);
    String getDefaultInfo();
}
