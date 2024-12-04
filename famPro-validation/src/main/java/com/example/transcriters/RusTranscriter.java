package com.example.transcriters;

import com.ibm.icu.text.Transliterator;

public class RusTranscriter implements AbstractTranscriter {


    public static final String RUSSIAN_LATIN_BGN = "Russian-Latin/BGN";
    public static final String LATIN_RUSSIAN_BGN = "Latin-Russian/BGN";


    @Override
    public String transcritWordToLocalisation(String str) {

        return Transliterator.getInstance(LATIN_RUSSIAN_BGN).transliterate(str);
    }
    @Override
    public String transcritWordFromLocalisation(String str) {
        return Transliterator.getInstance(RUSSIAN_LATIN_BGN).transliterate(str);
    }

    @Override
    public String getAbsent() {
        return "(Отсутствует в базе) ";
    }

    @Override
    public String getInfoNotFully() {
        return "(Информация не полна) ";
    }

    @Override
    public String getIncorrectInfo() {
        return "Неверная информация";
    }

    @Override
    public String getBirth() {
        return " . Дата рождения: ";
    }

    @Override
    public String getOut() {
        return "Информация отсутствует";
    }

    @Override
    public String empty() {
        return "<пусто>";
    }


}

