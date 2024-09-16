package com.example.service;


import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SenderMailService {
    private final DirectChannel sendMailChannel;
    private final JavaMailSender mailSender;
    private final String username;

    public SenderMailService(@Qualifier("emailSenderChannel") DirectChannel sendMailChannel, JavaMailSender mailSender, @Value("${spring.mail.username}")String username) {
        this.sendMailChannel = sendMailChannel;
        this.mailSender = mailSender;
        this.username = username;
    }

    public void sendingMessage(String sendTo) throws MessagingException {

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        MimeMailMessage mimeMailMessage = new MimeMailMessage(messageHelper);
        mimeMailMessage.setFrom(username);
        mimeMailMessage.setTo(sendTo);

        mimeMailMessage.setSubject("Mail Subject");

        String msg = "Это Email отправлено из сервиса: Integration";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        mimeMailMessage.getMimeMessage().setContent(multipart);
        Message<MimeMailMessage> message = MessageBuilder.withPayload(mimeMailMessage).build();
        System.out.println(message);
        sendMailChannel.send(message);
        log.info("message is done");
    }
}
