package ru.memman.enums;

public enum CheckStatus {
    OTHER("Generate by other user"),
    LINKED("Linked with SSO"),
    MANAGE("Person under manage"),
    MODERATE("Generate by moderator"),
    ABSENT("(Absent in base) "),
    NOT_FULLY("(Info not fully) "),
    UNCORRECTED("(Uncorrected Information)"),
    CHECKED("checked"),
    UNCHECKED("unchecked"),
    NUL("null"),
    PRESENT("present");
    private final String comment;

    CheckStatus(String comment) {
        this.comment = comment;
    }

    /**
     * Получение поля с комментарием статуса
     */
    public String getComment() {
        return comment;
    }
}
