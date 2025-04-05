package com.example.texts;

import com.example.enums.Localisation;
import com.example.models.SimpleUserInfo;
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
