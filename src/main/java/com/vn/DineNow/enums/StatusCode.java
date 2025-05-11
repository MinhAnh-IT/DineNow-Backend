package com.vn.DineNow.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum StatusCode {
    OK(200, "Request successful and data returned."),
    CREATED(201, "Resource created successfully."),
    BAD_REQUEST(400, "Invalid request or missing parameters."),
    DELETED(204, "Successfully deleted."),
    UNAUTHORIZED(405, "Authentication required."),
    UNAUTHORIZED_TOKEN(402, "Invalid or expired token."),
    FORBIDDEN(403, "Access forbidden."),
    NOT_FOUND(404, "Could not find %s with parameter %s"),
    EXIST_EMAIL(406, "A user with the email %s already exists."),
    EXIST_PHONE(407, "A user with the phone number %s already exists."),
    RESOURCE_IN_USE(408, "This %s is currently being referenced by another table."),
    LOGIN_FAILED(409, "Incorrect username or password."),
    INTERNAL_SERVER_ERROR(500, "Server encountered an error."),
    INVALID_INPUT(410, "Invalid input."),
    INVALID_OTP(411, "Invalid OTP."),
    RESET_TOKEN_EXPIRED(412, "OTP not verified or session has expired."),
    ACCOUNT_DISABLED(413, "Account is disabled."),
    INVALID_TOKEN(414, "Invalid token, or it has expired."),
    EMAIL_ERROR(415, "An error occurred while sending the email."),
    ALREADY_EXISTS(416, "Resource already exists"),
    RESTAURANT_NOT_APPROVED(417, "The restaurant is not approved or currently unavailable."),
    UNVERIFIED_ACCOUNT(418, "Account is not verified yet."),
    EXIST_NAME(419, "The name '%s' already exists in %s."),
    IMAGE_UPLOAD_FAILED(420, "Failed to upload image: %s"),
    IMAGE_DELETE_FAILED(421, "Failed to delete image: %s"),
    IMAGE_NOT_FOUND(422, "Image with ID %s not found"),
    INVALID_IMAGE_TYPE(423, "Invalid image type. Only PNG, JPG, JPEG, and GIF are allowed."),
    RESTAURANT_NOT_FOUND(424, "Restaurant with ID %s not found"),
    INVALID_ACTION(425, "Invalid action. %s"),
    INVALID_ENTITY(426, "Invalid entity. %s"),
    INVALID_REVIEW_ACTION(427, "You cannot review this %s because you haven't experienced it yet."),
    INVALID_DATE_RANGE(428, "Invalid date range. Start date must be before end date."),
    RUNTIME_EXCEPTION(500, "Unexpected runtime exception occurred.");


    int code;
    String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
