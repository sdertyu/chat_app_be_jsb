package com.pngo.chat_app.common.exception;

import com.pngo.chat_app.common.dto.response.ApiResponse;
import com.pngo.chat_app.common.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException ex) {
        // Handle runtime exceptions here
        ApiResponse response = new ApiResponse();
        response.setCode(ErrorCode.UNCATEGORIED_EXCEPTION.getCode());
        response.setMessage(ex.toString() + " - " + ex.getMessage());
//        response.setMessage(ErrorCode.UNCATEGORIED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        // Handle runtime exceptions here
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Handle validation exceptions here
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            // If the enum key is not found, use a default error code
//            errorCode = ErrorCode.INVALID_KEY;
        }

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


}
