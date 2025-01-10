package com.example.transcriters;

import java.util.Map;

public interface AbstractTranscriter {
String transcritWordToLocalisation(String str);
String transcritWordFromLocalisation(String str);
String getAbsent();
String getInfoNotFully();
String getIncorrectInfo();
String getBirthdayString();
String getOut();
String empty();
Map<String,String> getMatrixOfChange();
}
