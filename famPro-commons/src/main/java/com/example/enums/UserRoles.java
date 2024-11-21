package com.example.enums;

public enum UserRoles {
    ADMIN("Admin", "Full-low User"),
    MANAGER("Manager", "User in low"),
    VIP("Vip", "Premium user"),
    CHECKED("CheckedUser", "CheckedUser"),
    LINKED_USER("LinkedUser", "User with link to other user in the base"),
    BASE_USER("BaseUser", "User with minimal data to registered in the base"),
    SIMPLE_USER("SimpleUser", "User with only credentials");


    private final String commit;
    private final String nameSSO;

    UserRoles(String nameSSO, String commit) {
        this.nameSSO = nameSSO;
        this.commit = commit;
    }

    public String getCommit() {
        return commit;
    }

    public String getNameSSO() {
        return nameSSO;
    }
}
