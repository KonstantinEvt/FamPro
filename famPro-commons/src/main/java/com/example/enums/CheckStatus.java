package com.example.enums;

public enum CheckStatus {
    CHECKED("проверен"), UNCHECKED("не проверен");
    private final String rusStatus;
    CheckStatus(String rus) {
        this.rusStatus=rus;
    }
    /**
     * Получение поля с русским названием статуса
     */
    public String getRus(String rus) {return rusStatus;}
}
