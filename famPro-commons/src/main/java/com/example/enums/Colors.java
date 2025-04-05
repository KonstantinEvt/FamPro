package com.example.enums;

public enum Colors {
    RED("красный"),
    BROWN("коричневый"),
    BLACK("черный"),
    YELLOW("желтый"),
    WHITE("белый"),
    BLUE("голубой"),
    GREEN("зеленый");
    private final String comment;

    Colors(String comment) {
        this.comment = comment;
    }

    /**
     * Получение поля с комментарием статуса
     */
    public String getComment() {
        return comment;
    }
}
