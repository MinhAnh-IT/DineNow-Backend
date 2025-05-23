package com.vn.DineNow.services.common.email;

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

import java.util.HashMap;

/**
 * Service implementation for sending OTP or verification emails using a predefined HTML template.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {

    final JavaMailSender javaMailSender;
    final TemplateEngine templateEngine;

    @Value("${DineNow.mail.sender_name}")
    String sender;

    /**
     * Sends an email with an HTML template using Thymeleaf and includes a dynamic OTP.
     *
     * @param to           the recipient's email address
     * @param subject      the subject of the email
     * @param templateName the name of the Thymeleaf template to render
     * @param otp          the OTP to inject into the email content
     * @throws CustomException if any error occurs while sending the email
     */
    @Override
    @Async
    public void sendEmail(String to, String subject, String templateName, String otp) throws CustomException {
        try {
            // Prepare data model for Thymeleaf template
            Context context = new Context();
            context.setVariable("otp", otp);

            // Generate HTML email content from template
            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = javaMailSender.createMimeMessage();

            // true = multipart message, UTF-8 encoding
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Create a custom sender name in "From" field
            InternetAddress fromAddress = new InternetAddress("testproject610@gmail.com", sender, "UTF-8");
            helper.setFrom(fromAddress);

            helper.setTo(to);
            helper.setSubject(subject);

            // Send HTML as email body
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
    }

    /**
     * Sends a confirmation email using a Thymeleaf template with dynamic variables.
     *
     * @param to           the recipient's email address
     * @param subject      the subject of the email
     * @param templateName the name of the Thymeleaf template to render
     * @param variables    a map of variables to inject into the template
     * @throws CustomException if any error occurs while sending the email
     */
    @Override
    @Async
    public void confirmOrderEmail(String to, String subject, String templateName, HashMap<String, Object> variables) throws CustomException {
        try {
            Context context = new Context();
            variables.forEach(context::setVariable);

            String htmlContent = templateEngine.process(templateName, context);

            System.out.println("====== HTML EMAIL CONTENT ======");
            System.out.println(htmlContent);
            System.out.println("================================");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            InternetAddress fromAddress = new InternetAddress("testproject610@gmail.com", sender, "UTF-8");
            helper.setFrom(fromAddress);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Confirmation email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending confirmation email to {}: {}", to, e.getMessage());
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
    }


    /**
     * Sends a rejection email using a Thymeleaf template with dynamic variables.
     *
     * @param to           the recipient's email address
     * @param subject      the subject of the email
     * @param templateName the name of the Thymeleaf template to render
     * @param variables    a map of variables to inject into the template
     * @throws CustomException if any error occurs while sending the email
     */
    @Override
    @Async
    public void rejectOrderEmail(String to, String subject, String templateName, HashMap<String, Object> variables) throws CustomException {
        try {
            Context context = new Context();
            variables.forEach(context::setVariable);

            String htmlContent = templateEngine.process(templateName, context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            InternetAddress fromAddress = new InternetAddress("testproject610@gmail.com", sender, "UTF-8");
            helper.setFrom(fromAddress);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Rejection email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending rejection email to {}: {}", to, e.getMessage());
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
    }


    @Override
    @Async
    public void sendEmailOwnerAccountCreated(String to, String subject, String templateName, String userName, String password) throws CustomException {
        try {
            // Prepare data model for Thymeleaf template
            Context context = new Context();
            context.setVariable("username", userName);
            context.setVariable("password", password);

            // Generate HTML email content from template
            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = javaMailSender.createMimeMessage();

            // true = multipart message, UTF-8 encoding
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Create a custom sender name in "From" field
            InternetAddress fromAddress = new InternetAddress("testproject610@gmail.com", sender, "UTF-8");
            helper.setFrom(fromAddress);

            helper.setTo(to);
            helper.setSubject(subject);

            // Send HTML as email body
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
    }
}
