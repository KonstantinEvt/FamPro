package com.example.enums;

public enum Assignment {
    WORK("рабочий"), HOME("домашний"), MOBILE("мобильный");
    private final String rusAssignment;
    Assignment(String rus) {
        this.rusAssignment=rus;
    }
    /**
     * Получение поля с русским названием назначения
     */
    public String getRus(String rus) {return rusAssignment;}

}
