package com.example.enums;

public enum KafkaOperation {
    ADD("add"), RENAME("rename"), REMOVE("remove");
    private final String operation;
    KafkaOperation(String operation){this.operation=operation;
    }
    public String getString(){return operation;}
}
