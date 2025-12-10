package com.example.enums;

public enum Subject {
    RIGHTS("Rights problem","Отсутстуют права"),
    MODERATION_WARNING("Moderation warning","Предупреждение о модерации"),
    MODERATION_FATHER("Moderation warning","Предупреждение о модерации"),
    MODERATION_MOTHER("Moderation warning","Предупреждение о модерации"),
    MODERATION_CHILD("Moderation warning","Предупреждение о модерации"),
    LAST_UPDATE("Person changed","Персона изменена"),
    DUPLICATE("Duplicate information", "Дубликат информации"),
    DUPLICATE_HIDE("Duplicate information", "Дубликат информации"),
    DUPLICATE_OTHER("Duplicate information", "Дубликат информации"),
    DUPLICATE_OTHER_HIDE("Duplicate information", "Дубликат информации"),
    WRONG_INFO_PARENTS("Wrong information","Неверная информация"),
    WRONG_INFO_SEX("Wrong information","Неверная информация"),
    WRONG_INFO_DATE("Wrong information","Неверная информация"),
    WRONG_INFO_OTHER("Wrong information","Неверная информация"),
    VOTING("Voting","Голосование"),
    LINK("Linking","Линкование"),
    LINK_POSITIVE("Positive linking","Успешное связывание"),
    LINK_NEGATIVE("Negative linking","Неудачное связывание"),
    UNKNOWN("Unknown operation","Операция неизвестна"),
    NOT_FULLY("Information not fully","Информация не полна");



    private final String commit;
    private final String rusCommit;
    Subject(String commit, String rusCommit){
        this.commit=commit;
        this.rusCommit=rusCommit;
    }
    public String getCommit() {
        return commit;
    }
    public String getRus() {
        return rusCommit;
    }
}
