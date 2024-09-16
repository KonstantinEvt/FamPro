package com.example.config;

import com.example.models.Email;
import com.example.service.ReceiveEmailService;
import com.example.transformers.InputEmailTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.messaging.support.GenericMessage;

import java.util.Properties;

@Configuration
@EnableIntegration
public class MailReceiverConfig {

    @Value("${spring.mail.hostInput}")
    private String resiverHost;
    @Value("${spring.mail.portInput}")
    private Integer portInput;
    //Креденшены
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    // Store
    @Value("${spring.mail.mailbox}")
    private String mailbox;

    private final InputEmailTransformer inputEmailTransformer;
    private final ReceiveEmailService receiveEmailService;

    public MailReceiverConfig(InputEmailTransformer inputEmailTransformer, ReceiveEmailService receiveEmailService) {
        this.inputEmailTransformer = inputEmailTransformer;
        this.receiveEmailService = receiveEmailService;
    }

    @Bean
    @Qualifier(value = "inputEmailFlow")
    public IntegrationFlow inputEmailFlow(@Qualifier("imapMailProperties") Properties properties) {
        String userName = username.replace("@", "%40");
        String storeURL = String.format("imaps://%s:%s@%s:%s/%s", userName, password, resiverHost, portInput, mailbox);
        return IntegrationFlow.from(
                        Mail.imapInboundAdapter(storeURL)
                                .javaMailProperties(properties)
                                .shouldMarkMessagesAsRead(true)
                                .shouldDeleteMessages(false)
                                .simpleContent(true)
                                .autoCloseFolder(false),
                        e -> e.poller(
                                Pollers.fixedDelay(50000)
                        )
                )
                .transform(inputEmailTransformer)
                .handle(t -> receiveEmailService.printEmail((GenericMessage<Email>) t))
                .get();

    }

    @Bean
    @Qualifier("imapMailProperties")
    public Properties imapMailProperties() {
        Properties receiverProp = new Properties();
        receiverProp.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        receiverProp.put("mail.imap.socketFactory.fallback", false);
        receiverProp.put("mail.store.protocol", "imap");
        receiverProp.put("mail.debug", true);
        return receiverProp;
    }


}