package com.example.service;

import com.example.models.Email;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

//пока как загрушка и будет так долго :)
@Service
public class ReceiveEmailService {
    public void printEmail(GenericMessage<Email> email) {
        System.out.println("===============processing==============");
        System.out.println(email);

        System.out.println("===============final mail==============");
    }
}
