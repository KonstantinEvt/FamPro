package com.example.enums;

public enum CheckStatus {
    OTHER("Generate by other user"),
    LINKED("Linked with SSO"),
    MODERATE("Generate by moderator"),
    ABSENT("(Absent in base) "),
    NOT_FULLY("(Info not fully) "),
    UNCORRECTED("(Uncorrected Information)"),
    CHECKED("checked"),
    UNCHECKED("unchecked");
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
