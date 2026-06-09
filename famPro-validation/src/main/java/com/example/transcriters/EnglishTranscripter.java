package com.example.transcriters;


import com.example.enums.Attention;
import com.example.enums.CheckStatus;
import com.example.enums.Subject;
import com.example.enums.SwitchPosition;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class EnglishTranscripter implements AbstractTranscripter {
    public static final Map<String,String> matrixOfChange=new HashMap<>();
    public final Map<String,String> matrixOfTextGeneration= Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE.name(), "(En) Введенная основная информация соответствует другой персоне в базе. Его ID: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_HIDE.name(), "(En) Введенная основная информация соответствует альтернативному имени другой персоны в базе. Его ID: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER.name(), "(En) Введенное альтернативное имя идентично альтернативному имени другой персоны в базе. Данное имя игнорировано. ID персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE.name(), "(En) Введенное альтернативное имя соответствует другой персоне в базе. Данное имя игнорировано. ID персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.RIGHTS.name(), "(En) Вы не имеете прав на действия с данной персоной"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_CHILD.name(), "(En) Обнаруженый ребенок находится под модерацией/голосованием. Повторите Вашу попытку позже. Найденный ребенок: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_FATHER.name(), "(En) Обнаруженый отец находится под модерацией/голосованием. Введенные данные отца игнорированы. Введите эти данные позже. Найденный отец: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_MOTHER.name(), "(En) Обнаруженая мать находится под модерацией/голосованием. Введенные данные матери игнорированы. Введите эти данные позже. Найденная мать:  "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_WARNING.name(), "(En) Персона находится на модерации/голосовании. Повторите Вашу попытку позже. Персона: "),
            new AbstractMap.SimpleEntry<>(Subject.LAST_UPDATE.name(), "(En) Персона между Вашими запросами изменилась. Повторите Ваши запросы сначала. Персона: "),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_DATE.name(), "(En) Введенные даты рождения родителей не могут соответствовать действительности. Данный блок игнорирован."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_SEX.name(), "(En) Ошибка в введенном поле персоны."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_PARENTS.name(), "(En) Родители не могут иметь таковые имена."),
            new AbstractMap.SimpleEntry<>(Subject.VOTING.name(), "(En) Начато голосование на наличие взаимосвязи: "),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_OTHER.name(), "(En) Введенное альтернативное имя не корректно."),
            new AbstractMap.SimpleEntry<>(Subject.LINK.name(), "(En) Линкование пользователя с: "),
            new AbstractMap.SimpleEntry<>(Subject.LINK_POSITIVE.name(), "(En) Вы успешно связались с персоной: "),
            new AbstractMap.SimpleEntry<>(Subject.LINK_NEGATIVE.name(), "(En) Не удалась связь с персоной: "),
            new AbstractMap.SimpleEntry<>("USER_NICK", "(En) запрос пользователя с НИКОМ: "),
            new AbstractMap.SimpleEntry<>(Subject.NOT_FULLY.name(), "(En) Введенная информация не полна.")
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

