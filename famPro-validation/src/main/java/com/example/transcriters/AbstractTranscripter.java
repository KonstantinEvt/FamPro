package com.example.transcriters;

import com.example.enums.Attention;
import com.example.enums.Subject;
import com.example.enums.SwitchPosition;

import java.util.Map;

public interface AbstractTranscripter {
String transcritWordToLocalisation(String str);
String transcritWordFromLocalisation(String str);
String getAbsent();
String getInfoNotFully();
String getIncorrectInfo();
String getBirthdayString();
String getOut();
String getClose();
String empty();
Map<String,String> getMatrixOfChange();
String getTextSubject(Subject subject);
Map<String,String> getMatrixOfTextGeneration();
String getTextSwitchPosition(SwitchPosition switchPosition);
String getAnd();
}
