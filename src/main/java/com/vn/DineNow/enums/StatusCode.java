package com.vn.DineNow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    OK(200, "Request successful and data returned."),
    CREATED(201, "Resource created successfully."),
    BAD_REQUEST(400, "Invalid request or missing parameters."),
    INVALID_EMAIL(401, "Invalid %s format."),
    UNAUTHORIZED(405, "Authentication required."),
    UNAUTHORIZED_TOKEN(402, "Invalid or expired token."),
    FORBIDDEN(403, "Access forbidden."),
    NOT_FOUND(404, "Resource with %s %s not found."),
    EXIST_EMAIL(406, "A user with the email %s already exists."),
    EXIST_PHONE(407, "A user with the phone number %s already exists."),
    EXIST_USER(408, "A user with ID %d already exists."),
    LOGIN_FAILED(409, "Incorrect username or password."),
    INTERNAL_SERVER_ERROR(500, "Server encountered an error."),
    INVALID_INPUT(410, "Invalid input."),
    INVALID_OTP(411, "Invalid OTP."),
    RESET_TOKEN_EXPIRED(412, "OTP not verified or session has expired."),
    EMAIL_ERROR(413, "An error occurred while sending the email.");

    private final int code;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
