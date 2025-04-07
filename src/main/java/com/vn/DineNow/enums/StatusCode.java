package com.vn.DineNow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    OK(200, "Request successful and data returned."),
    CREATED(201, "Resource created successfully."),
    BAD_REQUEST(400, "Invalid request or missing parameters."),
    UNAUTHORIZED(401, "Authentication required."),
    FORBIDDEN(403, "Access forbidden."),
    NOT_FOUND(404, "Resource with %s %s not found."),
    EXIST_EMAIL(406, "A user with the email %s already exists."),
    EXIST_PHONE(407, "A user with the phone number %s already exists."),
    EXIST_USER(408, "A user with ID %d already exists."),
    INTERNAL_SERVER_ERROR(500, "Server encountered an error."),
    INVALID_PASSWORD(401, "Incorrect username or password.");

    private final int code;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
