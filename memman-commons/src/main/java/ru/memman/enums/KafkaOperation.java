package ru.memman.enums;

public enum KafkaOperation {
    ADD("add"), EDIT("edit"), RENAME("rename"), REMOVE("remove"), GET("get"), EXIT("exit");
    private final String operation;
    KafkaOperation(String operation){this.operation=operation;
    }
    public String getString(){return operation;}
}
