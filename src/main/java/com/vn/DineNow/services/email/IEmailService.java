package com.vn.DineNow.services.email;

import com.vn.DineNow.exception.CustomException;

public interface IEmailService {
    void sendEmail(String to, String subject, String templateName, String otp) throws CustomException;
}
