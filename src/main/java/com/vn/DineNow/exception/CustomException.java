package com.vn.DineNow.exception;

import com.vn.DineNow.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomException extends Exception {
    private StatusCode errorCode;

    // Constructor với errorCode và message
    public CustomException(StatusCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }

    // Constructor với errorCode và cause
    public CustomException(StatusCode errorCode, Throwable cause, Object... args) {
        super(errorCode.getMessage(args), cause);  // Truyền message và cause
        this.errorCode = errorCode;
    }
}
