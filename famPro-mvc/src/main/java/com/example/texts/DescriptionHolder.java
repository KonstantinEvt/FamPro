package com.example.texts;

import com.example.models.SimpleUserInfo;
import org.springframework.stereotype.Component;

@Component
public class DescriptionHolder {
    AbstractDescriptions descriptions;
    SimpleUserInfo simpleUserInfo;

    public void setDescriptionHolder() {
        if (simpleUserInfo.getLocalisation().equals("ru")) descriptions = new RusDescriptions();
        else descriptions = new EnDescriptions();
    }
}
