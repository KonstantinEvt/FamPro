package ru.memman.transcriters;

import com.ibm.icu.text.Transliterator;
import org.springframework.stereotype.Component;
import ru.memman.enums.Relation;
import ru.memman.enums.Subject;
import ru.memman.enums.SwitchPosition;

import java.util.AbstractMap;
import java.util.Map;

public class RusTranscripter implements AbstractTranscripter {


    private static final String RUSSIAN_LATIN_BGN = "Russian-Latin/BGN";
    private static final String LATIN_RUSSIAN_BGN = "Latin-Russian/BGN";

    private final Map<String, String> matrixOfChange = Map.ofEntries(
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

    public final Map<String, String> matrixOfTextGeneration = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE.name().concat("1"), "Введенная основная информация:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE.name().concat("2"), "</em><br>соответствует другой персоне в базе. Ввод данных отменен.<br><strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_HIDE.name().concat("1"), "Введенная основная информация:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_HIDE.name().concat("2"), "</em><br>соответствует альтернативному имени другой персоны в базе. Ввод данных отменен.<br><strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER.name().concat("1"), "Введенное альтернативное имя: <br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER.name().concat("2"), "</em><br>идентично альтернативному имени другой персоны в базе. Данное имя игнорировано.<br><strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE.name().concat("1"), "Введенное альтернативное имя:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE.name().concat("2"), "</em><br>соответствует другой персоне в базе. Данное имя игнорировано.<br><strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HARD.name().concat("1"), "Введенное или бывшее альтернативное имя:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HARD.name().concat("2"), "</em><br>в совокупности с изменением даты рождения идентично альтернативному имени другой персоны в базе. Ввод данных отменен.<br><strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE_HARD.name().concat("1"), "Введенное или бывшее альтернативное имя:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE_HARD.name().concat("2"), "</em><br>в совокупности с изменением даты рождения соответствует другой персоне в базе. Ввод данных отменен.<br><strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.RIGHTS.name().concat("1"), "Вы не имеете прав на действия с персоной:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.RIGHTS.name().concat("2"), "</em><br>Ввод данных отменен."),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_CHILD.name().concat("1"), "Обнаруженый ребенок:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_CHILD.name().concat("2"), "</em><br>находится под модерацией/голосованием. Ввод данных отменен. Повторите Вашу попытку позже.<br><strong>ID</strong> ребенка:"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_FATHER.name().concat("1"), "Обнаруженый отец:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_FATHER.name().concat("2"), "</em><br>находится под модерацией/голосованием. Введенные данные отца игнорированы. Введите эти данные позже.<br><strong>ID</strong> отца: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_MOTHER.name().concat("1"), "Обнаруженая мать:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_MOTHER.name().concat("2"), "</em><br>находится под модерацией/голосованием. Введенные данные матери игнорированы. Введите эти данные позже.<br><strong>ID</strong> матери:  "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_WARNING.name().concat("1"), "Персона:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_WARNING.name().concat("2"), "</em><br>находится на модерации/голосовании. Ввод данных отменен. Повторите Вашу попытку позже.<strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.LAST_UPDATE.name().concat("1"), "Персона:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.LAST_UPDATE.name().concat("2"), "</em><br>между Вашими запросами изменилась. Запросы прерваны. Повторите их сначала.<strong>ID</strong> персоны: "),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_DATE.name().concat("1"), "Введенные даты рождения родителей:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_DATE.name().concat("2"), "</em><br>не могут соответствовать действительности. Данный блок игнорирован."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_SEX.name().concat("1"), "Ошибка в введенном поле персоны:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_SEX.name().concat("2"), "</em><br>Ввод данных отменен."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_PARENTS.name().concat("1"), "Родители не могут иметь таковые имена:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_PARENTS.name().concat("2"), "</em><br>Ввод данных отменен."),
            new AbstractMap.SimpleEntry<>(Subject.VOTING.name().concat("1"), "Начато голосование на наличие взаимосвязи между:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.VOTING.name().concat("2"), "</em><br>и<br>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_OTHER.name().concat("1"), "Введенное альтернативное имя:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_OTHER.name().concat("2"), "</em><br>некорректно."),
            new AbstractMap.SimpleEntry<>(Subject.LINK.name().concat("1"), "Вы запросили связывание себя с персоной:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.LINK.name().concat("2"), "</em><br>Голосование начато."),
            new AbstractMap.SimpleEntry<>(Subject.LINK_POSITIVE.name().concat("1"), "Успешное связывание пользователя с персоной:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.LINK_POSITIVE.name().concat("2"), "</em><br>Поздравляем"),
            new AbstractMap.SimpleEntry<>(Subject.LINK_NEGATIVE.name().concat("1"), "Cвязывание пользователя с персоной:<br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.LINK_NEGATIVE.name().concat("2"), "</em><br>не удалось."),
            new AbstractMap.SimpleEntry<>("USER_NICK", "Пользователь с ником <em>(на момент запроса)</em>: <strong>"),
            new AbstractMap.SimpleEntry<>("USER_NICK1", "</strong> хочет "),
            new AbstractMap.SimpleEntry<>("USER_NICK2", "cвязать сябя с персоной:<br>"),
            new AbstractMap.SimpleEntry<>("USER_NICK3", "установить родственную связь между:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.NOT_FULLY.name().concat("1"), "Введенная информация: <br><em>"),
            new AbstractMap.SimpleEntry<>(Subject.NOT_FULLY.name().concat("2"), "</em><br>неполна."),
            new AbstractMap.SimpleEntry<>("end_voting", "Голосование завершено")
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
    public String getAbsentLocalisation() {
        return "<em>(Это сообщение не имеет Вашей локализации)</em><br>";
    }

    @Override
    public String getAbsent() {
        return "отсутствующими в базе родителями";
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
    public String getElection() {
        return "Вы подтверждаете подобное действие?";
    }

    @Override
    public String getUnknown() {
        return "Неизестный";
    }

    @Override
    public String getUser() {
        return "Пользователь";
    }

    @Override
    public String getPerson() {
        return "персона";
    }

    @Override
    public String getRequest() {
        return "Ваш запрос ";
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

    @Override
    public String getRelation(Relation relation) {
        return relation.getRus();
    }

}

