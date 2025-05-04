package com.vn.DineNow.services.common.email;

import com.vn.DineNow.exception.CustomException;

public interface EmailService {
    void sendEmail(String to, String subject, String templateName, String otp) throws CustomException;
    void sendEmailOwnerAccountCreated(String to, String subject, String templateName, String userName, String password) throws CustomException;
}
