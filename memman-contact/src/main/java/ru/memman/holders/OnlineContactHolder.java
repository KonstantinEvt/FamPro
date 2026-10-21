package ru.memman.holders;

import ru.memman.dtos.ContactDto;
import ru.memman.entity.Contact;
import ru.memman.entity.RealMen;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
@Setter
public class OnlineContactHolder {
    private final Map<String,RealMen> onlineManMap;
    private final Map<String,Collection<Contact>> onlineContactsMap;

    public OnlineContactHolder(Map<String, RealMen> onlineManMap,
                               Map<String, Collection<Contact>> onlineContactsMap) {
        this.onlineManMap = onlineManMap;
        this.onlineContactsMap = onlineContactsMap;
    }
}
