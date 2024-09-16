package com.example.service;


import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class SenderMailService {
    private final DirectChannel sendMailChannel;
    private final JavaMailSender mailSender;

    public SenderMailService(@Qualifier("emailSenderChannel") DirectChannel sendMailChannel, JavaMailSender mailSender) {
        this.sendMailChannel = sendMailChannel;
        this.mailSender = mailSender;
    }

    public void sendingMessage() throws MessagingException {

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        MimeMailMessage mimeMailMessage = new MimeMailMessage(messageHelper);
        mimeMailMessage.setFrom("kyevt@bk.ru");
        mimeMailMessage.setTo("k-s-y@yandex.ru");
        mimeMailMessage.setSubject("Mail Subject");

        String msg = "Ура!!!! This is my email using IntegrationFlow channel";
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        mimeMailMessage.getMimeMessage().setContent(multipart);
        Message<MimeMailMessage> message = MessageBuilder.withPayload(mimeMailMessage).build();
        System.out.println(message);
        sendMailChannel.send(message);
        System.out.println("message is done");
    }
}
