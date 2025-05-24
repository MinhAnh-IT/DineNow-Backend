package com.vn.DineNow.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailConfig {

    @Value("${DineNow.mail.host}")
    String host;

    @Value("${DineNow.mail.port}")
    int port;

    @Value("${DineNow.mail.username}")
    String username;

    @Value("${DineNow.mail.password}")
    String password;

    @Value("${DineNow.mail.properties.mail.smtp.auth}")
    String smtpAuth;

    @Value("${DineNow.mail.properties.mail.smtp.starttls.enable}")
    String starttlsEnable;

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
