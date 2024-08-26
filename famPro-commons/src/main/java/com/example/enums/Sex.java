package com.example.enums;

public enum Sex {
    MALE("Мужской"), FEMALE("Женский");
private final String rusSex;
    Sex(String rus) {
        this.rusSex=rus;
    }
    /**
     * Получение поля с русским названием пола
     */
   public String getRus(String rus) {return rusSex;}
}
