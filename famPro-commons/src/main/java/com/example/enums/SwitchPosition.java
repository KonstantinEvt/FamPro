package com.example.enums;

public enum SwitchPosition {
    PRIME("photo of person", "фото персоны"),
    BIRTH("photo of birth place", "фото места рождения"),
    BURIAL("photo of burial place", "фото места захоронения"),
    ADDRESS("photo of address", "фото места проживания"),
    MAIN("link with parents", "связь с родителями семьи:"),
    FATHER("link with father", "связь с отцом"),
    MOTHER("link with mother", "связь с матерью"),
    CHILD("link with child", "связь с ребенком");
    private final String commit;
    private final String rus;

    SwitchPosition(String commit, String rus) {
        this.commit = commit;
        this.rus = rus;
    }

    public String getCommit() {
        return commit;
    }

    ;

    public String getRus() {
        return rus;
    }

    ;
}
