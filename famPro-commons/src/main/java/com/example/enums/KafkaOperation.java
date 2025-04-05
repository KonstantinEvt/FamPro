package com.example.enums;

public enum KafkaOperation {
    ADD("add"), EDIT("edit"), RENAME("rename"), REMOVE("remove"), GET("get");
    private final String operation;
    KafkaOperation(String operation){this.operation=operation;
    }
    public String getString(){return operation;}
}
