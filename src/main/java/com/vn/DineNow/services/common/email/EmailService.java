package com.vn.DineNow.services.common.email;

import com.vn.DineNow.exception.CustomException;

import java.util.HashMap;

public interface EmailService {
    void sendEmail(String to, String subject, String templateName, String otp) throws CustomException;
    void confirmOrderEmail(String to, String subject, String templateName, HashMap<String, Object> variables) throws CustomException;
    void rejectOrderEmail(String to, String subject, String templateName, HashMap<String, Object> variables) throws CustomException;
    void sendEmailOwnerAccountCreated(String to, String subject, String templateName, String userName, String password) throws CustomException;
}
