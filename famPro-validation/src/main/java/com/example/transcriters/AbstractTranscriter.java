package com.example.transcriters;

public interface AbstractTranscriter {
String transcritWordToLocalisation(String str);
String transcritWordFromLocalisation(String str);
String getAbsent();
String getInfoNotFully();
String getIncorrectInfo();
String getBirth();
String getOut();
String empty();
}
