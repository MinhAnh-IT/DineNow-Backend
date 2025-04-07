package com.vn.DineNow.exception;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.response.APIResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<APIResponse<String>> handleCustomException(CustomException ex) {
        APIResponse<String> response = APIResponse.<String>builder()
                .status(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<APIResponse<String>> handleGeneralException(Exception ex) {
//        APIResponse<String> response = APIResponse.<String>builder()
//                .status(StatusCode.INTERNAL_SERVER_ERROR.getCode())
//                .message(StatusCode.INTERNAL_SERVER_ERROR.getMessage())
//                .data(null)
//                .build();
//        return ResponseEntity.ok(response);
//    }


}
