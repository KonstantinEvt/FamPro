package com.example.transcriters;

import com.ibm.icu.text.Transliterator;

import java.util.AbstractMap;
import java.util.Map;

public class RusTranscriter implements AbstractTranscriter {


    public static final String RUSSIAN_LATIN_BGN = "Russian-Latin/BGN";
    public static final String LATIN_RUSSIAN_BGN = "Latin-Russian/BGN";

    public static final Map<String,String> matrixOfChange= Map.ofEntries(
            new AbstractMap.SimpleEntry<>("А", "аллея "),
            new AbstractMap.SimpleEntry<>("Ал", "аллея "),
            new AbstractMap.SimpleEntry<>("Аллея", "аллея "),

            new AbstractMap.SimpleEntry<>("Г", "город "),
            new AbstractMap.SimpleEntry<>("Гор", "город "),
            new AbstractMap.SimpleEntry<>("Город", "город "),

            new AbstractMap.SimpleEntry<>("Д", "дом "),
            new AbstractMap.SimpleEntry<>("Дом", "дом "),
            new AbstractMap.SimpleEntry<>("Дер", "деревня "),
            new AbstractMap.SimpleEntry<>("Деревня", "деревня "),

            new AbstractMap.SimpleEntry<>("К", "корпус "),
            new AbstractMap.SimpleEntry<>("Кор", "корпус "),
            new AbstractMap.SimpleEntry<>("Корп", "корпус "),
            new AbstractMap.SimpleEntry<>("Корпус", "корпус "),

            new AbstractMap.SimpleEntry<>("Кв", "квартира "),
            new AbstractMap.SimpleEntry<>("Квартира", "квартира "),

            new AbstractMap.SimpleEntry<>("Пос", "поселок "),
            new AbstractMap.SimpleEntry<>("Поселок", "поселок "),
            new AbstractMap.SimpleEntry<>("Посёлок", "поселок "),

            new AbstractMap.SimpleEntry<>("Пр", "проспект "),
            new AbstractMap.SimpleEntry<>("Просп", "проспект "),
            new AbstractMap.SimpleEntry<>("Проспект", "проспект "),
            new AbstractMap.SimpleEntry<>("Проезд", "проезд "),
            new AbstractMap.SimpleEntry<>("Пер", "переулок "),
            new AbstractMap.SimpleEntry<>("Переулок", "переулок "),

            new AbstractMap.SimpleEntry<>("У", "улица "),
            new AbstractMap.SimpleEntry<>("Ул", "улица "),
            new AbstractMap.SimpleEntry<>("Улица", "улица "),

            new AbstractMap.SimpleEntry<>("Ш", "шоссе "),
            new AbstractMap.SimpleEntry<>("Шос", "шоссе "),
            new AbstractMap.SimpleEntry<>("Шоссе", "шоссе ")
            );

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
    public String getBirthdayString() {
        return " . Дата рождения: ";
    }

    @Override
    public String getOut() {
        return "Информация отсутствует";
    }

    @Override
    public String getClose() {
        return "Скрыто";
    }

    @Override
    public String empty() {
        return "<пусто>";
    }

    @Override
    public Map<String, String> getMatrixOfChange() {
        return matrixOfChange;
    }


}

