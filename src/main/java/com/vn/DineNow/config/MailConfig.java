package com.vn.DineNow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${DineNow.mail.host}")
    private String host;

    @Value("${DineNow.mail.port}")
    private int port;

    @Value("${DineNow.mail.username}")
    private String username;

    @Value("${DineNow.mail.password}")
    private String password;

    @Value("${DineNow.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${DineNow.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);

        return mailSender;
    }
}
