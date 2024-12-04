package org.example.texts;

import com.example.enums.UserRoles;
import lombok.Getter;

import java.util.Map;
@Getter
public class RusDescriptions implements AbstractDescriptions {
private Map<String,String> descriptionToCabinetPage;

    @Override
    public String getDescriptionToRole(UserRoles userRole) {
        return switch (userRole) {
            case ADMIN -> "Это пользователь с правами на всё";

            case MANAGER -> "Пользователь с доступом к редактированию контента базы";

            case VIP -> "Пользователь с особыми правами";

            case CHECKED -> "Подтвержденный пользователь";
            case LINKED_USER -> "Пользователь связанный с записью в базе";
            case BASE_USER -> "Пользователь заполнивший поля ФИО и день рождения";
            case SIMPLE_USER -> "Зарегистрипованный пользователь";
        };
    }
}
