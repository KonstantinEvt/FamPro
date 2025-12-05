package com.example.service;

import com.example.dtos.DirectiveGuards;
import com.example.entity.Notification;
import com.example.enums.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service

public class FormAndSendService {
    private final LinkedList<DirectiveGuards> directiveGuardsList;

    public FormAndSendService(@Qualifier("directiveGuards") LinkedList<DirectiveGuards> directiveGuardsList) {
        this.directiveGuardsList = directiveGuardsList;
    }

    public void sendNotification(String tokenUser, Attention attention, String person, Long id, Subject subject, Localisation localisation) {
        DirectiveGuards dd = DirectiveGuards.builder()
                .tokenUser(tokenUser)
                .operation(KafkaOperation.ADD)
                .person(person)
                .number1(id)
                .created(new Timestamp(System.currentTimeMillis()))
                .localisation(localisation)
                .subject(subject)
                .build();
        switch (attention) {
            case NEGATIVE -> {
                dd.setSwitchPosition(SwitchPosition.MOTHER);
            }
            case MODERATE -> {
                dd.setSwitchPosition(SwitchPosition.MAIN);
            }
            case RIGHTS -> {
                dd.setSwitchPosition(SwitchPosition.FATHER);
            }
            case POSITIVE -> dd.setSwitchPosition(SwitchPosition.BIRTH);

            default -> dd.setSwitchPosition(SwitchPosition.ADDRESS);

        }
        directiveGuardsList.add(dd);
    }

    public void sendAllNotifications(String token, Localisation localisation, List<Notification> notificationList) {
        for (Notification notification :
                notificationList) {
            DirectiveGuards dd = DirectiveGuards.builder()
                    .tokenUser(token)
                    .operation(KafkaOperation.ADD)
                    .created(new Timestamp(System.currentTimeMillis()))
                    .localisation(localisation)
                    .subject(notification.getSubject())
                    .build();
            if (notification.getPerson()!=null) dd.setPerson(notification.getPerson());
            if (notification.getId()!=null) dd.setNumber1(notification.getId());
            switch (notification.getAttention()) {
                case NEGATIVE -> {
                    dd.setSwitchPosition(SwitchPosition.MOTHER);
                }
                case MODERATE -> {
                    dd.setSwitchPosition(SwitchPosition.MAIN);
                }
                case RIGHTS -> {
                    dd.setSwitchPosition(SwitchPosition.FATHER);
                }
                case POSITIVE -> dd.setSwitchPosition(SwitchPosition.BIRTH);

                default -> dd.setSwitchPosition(SwitchPosition.ADDRESS);

            }
            directiveGuardsList.add(dd);
        }
    }
}
