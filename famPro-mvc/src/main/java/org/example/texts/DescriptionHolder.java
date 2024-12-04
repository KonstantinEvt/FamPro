package org.example.texts;

import org.example.models.SimpleUserInfo;
import org.springframework.stereotype.Component;

@Component
public class DescriptionHolder {
    AbstractDescriptions descriptions;
    SimpleUserInfo simpleUserInfo;

    public void setDescriptionHolder() {
        if (simpleUserInfo.getLocalisation().equals("loc=ru")) descriptions = new RusDescriptions();
        else descriptions = new EnDescriptions();
    }
}
