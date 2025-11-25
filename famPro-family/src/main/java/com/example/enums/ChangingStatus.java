package com.example.enums;

public enum ChangingStatus {
    NONE("fields are empty"),
    NOT_IMPORTANT("not linking"),
    ABSENT("changes are absent. person is absent or exist"),
    FREE("link (absent)"),
    SET("link new person"),
    LIGHT_FREE("remove (absent)"),
    HARD_FREE("remove (absent) + link (absent)"),
    REMOVE("remove link with person"),
    MINOR_CHANGE("remove + free"),
    CHANGE("remove (absent) + set"),
    MAJOR_CHANGE("remove + set");

    private final String info;

    ChangingStatus(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    ;
}
