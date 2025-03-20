package com.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceFam {
    Session getSession;
    public void sendingMessage() throws MessagingException {

        Message message = new MimeMessage(getSession);
        message.setFrom(new InternetAddress("kyevt@bk.ru"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("k-s-y@yandex.ru"));
        message.setSubject("Mail Subject");
        String msg = "Email is going out from service: Notification";
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        System.out.println(getSession.getProperties());
        Transport.send(message);
        System.out.println("message is done");
    }



}
