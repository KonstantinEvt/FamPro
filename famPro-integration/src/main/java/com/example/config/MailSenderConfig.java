package com.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.MailSendingMessageHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableIntegration

public class MailSenderConfig {

    @Value("${spring.mail.host}")
    private String sendingHost;
    @Value("${spring.mail.port}")
    private Integer portOutput;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String authSmpt;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsSmpt;
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private String sslEnable;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;



    @Bean
    @Qualifier(value = "outputEmailFlow")
    public IntegrationFlow outputEmailFlow(JavaMailSender mailSender) {
        return IntegrationFlow.from("emailSenderChannel")
                .handle(new MailSendingMessageHandler(mailSender))
                .get();
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(sendingHost);
        mailSender.setPort(portOutput);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.getJavaMailProperties().put("mail.transport.protocol", "smtp");
        mailSender.getJavaMailProperties().put("mail.smtp.auth", authSmpt);
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", starttlsSmpt);
        mailSender.getJavaMailProperties().put("mail.smtp.ssl.enable", sslEnable);
        return mailSender;
    }
    @Bean("emailSenderChannel")
    public DirectChannel senderChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setDatatypes(org.springframework.mail.javamail.MimeMailMessage.class);
        return directChannel;

}
}
