package com.pngo.chat_app.exception;

import com.pngo.chat_app.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    // This class can be used to handle exceptions globally across the application.
    // You can define methods here to handle specific exceptions and return custom responses.
    // For example, you can use @ExceptionHandler annotations to catch specific exceptions
    // and return a meaningful error response to the client.

    // Example:
    // @ExceptionHandler(UserNotFoundException.class)
    // public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    // }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException ex) {
        // Handle runtime exceptions here
        ApiResponse response = new ApiResponse();
        response.setCode(ErrorCode.UNCATEGORIED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIED_EXCEPTION.getMessage());
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
