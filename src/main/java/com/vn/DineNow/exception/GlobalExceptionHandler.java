package com.vn.DineNow.exception;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.response.APIResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vn.DineNow.enums.StatusCode.INVALID_INPUT;

@ControllerAdvice
public class  GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<APIResponse<String>> handleCustomException(CustomException ex) {
        APIResponse<String> response = APIResponse.<String>builder()
                .status(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        APIResponse<Map<String, String>> response = APIResponse.<Map<String, String>>builder()
                .status(INVALID_INPUT.getCode())
                .message(INVALID_INPUT.getMessage())
                .data(errors)
                .build();
        return ResponseEntity.ok(response);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<APIResponse<String>> handleNullPointerException(NullPointerException ex){
        APIResponse<String> response = APIResponse.<String>builder()
                .status(StatusCode.BAD_REQUEST.getCode())
                .message("A null reference was accessed")
                .build();
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        APIResponse<String> response = APIResponse.<String>builder()
                .status(StatusCode.RESTAURANT_NOT_APPROVED.getCode())
                .message(message)
                .build();

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<String>> handleRuntimeException(RuntimeException ex) {
        APIResponse<String> response = APIResponse.<String>builder()
                .status(StatusCode.RUNTIME_EXCEPTION.getCode())
                .message("Unexpected error occurred: " + ex.getMessage())
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
