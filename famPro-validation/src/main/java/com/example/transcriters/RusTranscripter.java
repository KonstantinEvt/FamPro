package com.example.transcriters;

import com.example.enums.Subject;
import com.example.enums.SwitchPosition;
import com.ibm.icu.text.Transliterator;

import java.util.AbstractMap;
import java.util.Map;
public class RusTranscripter implements AbstractTranscripter {


    private static final String RUSSIAN_LATIN_BGN = "Russian-Latin/BGN";
    private static final String LATIN_RUSSIAN_BGN = "Latin-Russian/BGN";

    private final Map<String,String> matrixOfChange= Map.ofEntries(
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
            new AbstractMap.SimpleEntry<>("Шоссе", "шоссе "));

    public final Map<String,String> matrixOfTextGeneration= Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE.name(), "Введенная основная информация соответствует другой персоне в базе. Его ID: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_HIDE.name(), "Введенная основная информация соответствует альтернативному имени другой персоны в базе. Его ID: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER.name(), "Введенное альтернативное имя идентично альтернативному имени другой персоны в базе. Данное имя игнорировано. ID персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE.name(), "Введенное альтернативное имя соответствует другой персоне в базе. Данное имя игнорировано. ID персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.RIGHTS.name(), "Вы не имеете прав на действия с данной персоной"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_CHILD.name(), "Обнаруженый ребенок находится под модерацией/голосованием. Повторите Вашу попытку позже. Найденный ребенок: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_FATHER.name(), "Обнаруженый отец находится под модерацией/голосованием. Введенные данные отца игнорированы. Введите эти данные позже. Найденный отец: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_MOTHER.name(), "Обнаруженая мать находится под модерацией/голосованием. Введенные данные матери игнорированы. Введите эти данные позже. Найденная мать:  "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_WARNING.name(), "Персона находится на модерации/голосовании. Повторите Вашу попытку позже. Персона: "),
            new AbstractMap.SimpleEntry<>(Subject.LAST_UPDATE.name(), "Персона между Вашими запросами изменилась. Повторите Ваши запросы сначала. Персона: "),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_DATE.name(), "Введенные даты рождения родителей не могут соответствовать действительности. Данный блок игнорирован."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_SEX.name(), "Ошибка в введенном поле персоны."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_PARENTS.name(), "Родители не могут иметь таковые имена."),
            new AbstractMap.SimpleEntry<>(Subject.VOTING.name(), "Начато голосование на наличие взаимосвязи: "),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_OTHER.name(), "Введенное альтернативное имя не корректно."),
            new AbstractMap.SimpleEntry<>(Subject.LINK.name(), "Линкование пользователя с: "),
            new AbstractMap.SimpleEntry<>(Subject.LINK_POSITIVE.name(), "Вы успешно связались с персоной: "),
            new AbstractMap.SimpleEntry<>(Subject.LINK_NEGATIVE.name(), "Не удалась связь с персоной: "),
            new AbstractMap.SimpleEntry<>("USER_NICK", "запрос пользователя с НИКОМ: "),
            new AbstractMap.SimpleEntry<>(Subject.NOT_FULLY.name(), "Введенная информация не полна.")
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

    @Override
    public String getTextSubject(Subject subject) {
        return subject.getRus();
    }
    @Override
    public Map<String, String> getMatrixOfTextGeneration() {
        return matrixOfTextGeneration;
    }

    @Override
    public String getTextSwitchPosition(SwitchPosition switchPosition) {
        return switchPosition.getRus();
    }

    @Override
    public String getAnd() {
        return " и ";
    }
}

