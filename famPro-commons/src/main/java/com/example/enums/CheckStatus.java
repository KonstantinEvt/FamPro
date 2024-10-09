package com.example.enums;

public enum CheckStatus {
    ABSENT("(Absent in base) "), NOT_FULLY("(Info not fully) "), CHECKED("checked"), UNCHECKED("unchecked");
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
