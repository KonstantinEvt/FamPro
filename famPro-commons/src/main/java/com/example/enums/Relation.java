package com.example.enums;

public enum Relation {
    PERSON("Person", "Персона"),
    MOTHER("Mother", "Мать"),
    FATHER("Father", "Отец"),
    GRANDMOTHER("Grandmother", "Бабушка"),
    GRANDFATHER("Grandfather", "Дедушка"),
    BROTHER("Brother", "Брат"),
    SISTER("Sister", "Сестра"),
    HALF_BROTHER_BY_FATHER("Half brother by father", "Единокровный брат"),
    HALF_BROTHER_BY_MOTHER("Half brother by mother", "Единоутробный брат"),
    HALF_SISTER_BY_FATHER("Half sister by father", "Единокровная сестра"),
    HALF_SISTER_BY_MOTHER("Half sister by mother", "Единоутробная сестра"),
    DAUGHTER("Daughter", "Дочь"),
    SON("Son", "Сын"),
    UNCLE("Uncle", "Дядя"),
    AUNT("Aunt", "Тетя"),
    NEPHEW("Nephew", "Племянник"),
    NIECE("Niece", "Племянница"),
    HALF_NEPHEW_BY_FATHER("Half nephew by father", "Единокровный племянник"),
    HALF_NEPHEW_BY_MOTHER("Half nephew by mother", "Единоутробный племянник"),
    HALF_NIECE_BY_FATHER("Half niece by father", "Единокровная племянница"),
    HALF_NIECE_BY_MOTHER("Half niece by mother", "Единоутробная племянница"),
    HALF_UNCLE_BY_FATHER("Half uncle by father", "Единокровный дядя"),
    HALF_UNCLE_BY_MOTHER("Half uncle by mother", "Единоутробный дядя"),
    HALF_AUNT_BY_FATHER("Half aunt by father", "Единокровная тетя"),
    HALF_AUNT_BY_MOTHER("Half aunt by mother", "Единоутробная тетя"),
    ANCESTOR("Ancestor", "Предок"),
    DESCENDANT("Descendant", "Потомок"),
    RELATIVE("Relative", "Родственник"),
    GRANDSON("Grandson","Внук"),
    GRANDDAUGHTER("Granddaughter","Внучка"),
    OTHER("Free relation", "Другая связь");


    private final String commit;
    private final String rusCommit;

    Relation(String commit, String rus) {
        this.commit = commit;
        this.rusCommit = rus;
    }

    public String getCommit() {
        return commit;
    }

    public String getRus() {
        return rusCommit;
    }
}
