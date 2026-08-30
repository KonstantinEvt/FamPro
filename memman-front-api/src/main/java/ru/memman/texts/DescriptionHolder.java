package ru.memman.texts;

import ru.memman.enums.Localisation;
import ru.memman.models.SimpleUserInfo;
import org.springframework.stereotype.Component;

@Component
public class DescriptionHolder {
    AbstractDescriptions descriptions;
    SimpleUserInfo simpleUserInfo;

    public void setDescriptionHolder() {
        if (simpleUserInfo.getLocalisation()== Localisation.RU) descriptions = new RusDescriptions();
        else descriptions = new EnDescriptions();
    }
}
