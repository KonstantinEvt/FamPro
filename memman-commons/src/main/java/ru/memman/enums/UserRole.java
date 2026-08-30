package ru.memman.enums;
public enum UserRole {
    ADMIN("Admin", "Админ","Full-low User"),
    MANAGER("Manager", "Менеджер","User in low"),
    VIP("Vip", "Привелигерованный","Premium user"),
    CHECKED("CheckedUser", "Проверенный","CheckedUser"),
    LINKED_USER("LinkedUser", "Связанный","User with link to other user in the base"),
    BASE_USER("BaseUser", "Базовый","User with minimal data to registered in the base"),
    SIMPLE_USER("SimpleUser", "Простой","User with only credentials");


    private final String commit;
    private final String nameSSO;
    private final String rusName;

    UserRole(String nameSSO, String rusName, String commit) {
        this.nameSSO = nameSSO;
        this.commit = commit;
        this.rusName = rusName;
    }

    public String getCommit() {
        return commit;
    }

    public String getNameSSO() {
        return nameSSO;
    }

    public String getRusName() {
        return rusName;
    }
}
