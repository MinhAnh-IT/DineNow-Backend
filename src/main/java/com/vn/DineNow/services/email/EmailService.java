package com.vn.DineNow.services.email;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailService implements IEmailService {

    final JavaMailSender javaMailSender;
    final TemplateEngine templateEngine;

    @Value("${DineNow.mail.sender_name}")
    String sender;

    @Override
    @Async
    public void sendEmail(String to, String subject, String templateName, String otp) throws CustomException {
        try {
            Context context = new Context();
            context.setVariable("otp", otp);

            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            InternetAddress fromAddress = new InternetAddress("testproject610@gmail.com", sender, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
    }
}
