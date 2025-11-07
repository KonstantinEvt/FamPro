package com.example.enums;

public enum SwitchPosition {
    PRIME("фото персоны"),
    BIRTH("фото места рождения"),
    BURIAL("фото места захоронения"),
    ADDRESS("фото места проживания"),
    MAIN("связь с родителями семьи:"),
    FATHER("связь с отцом"),
    MOTHER("связь с матерью"),
    CHILD("связь с ребенком");
    private final String info;
    SwitchPosition(String info){
        this.info=info;
    }
    public String getInfo(){
        return info;
    };
}
