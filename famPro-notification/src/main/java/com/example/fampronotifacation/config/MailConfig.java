package com.example.fampronotifacation.config;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String sendingHost;
    @Value("${spring.mail.port}")
    private Integer portOutput;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String authSmpt;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsSmpt;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;



    @Bean
    Properties getProp() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", authSmpt);
        prop.put("mail.smtp.starttls.enable", starttlsSmpt);
        prop.put("mail.smtp.host", sendingHost);
        prop.put("mail.smtp.port", portOutput);
        prop.put("mail.smtp.ssl.enable", true);
        prop.put("mail.smtp.ssl.trust", "*");
        return prop;
    }

    @Bean
    Session getSession() {
        return Session.getInstance(getProp(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}