package com.jeffersonvilla.HabitsTracker.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jeffersonvilla.HabitsTracker.exceptions.EmailInUseException;
import com.jeffersonvilla.HabitsTracker.exceptions.PasswordFormatException;
import com.jeffersonvilla.HabitsTracker.exceptions.UsernameInUseException;
import com.jeffersonvilla.HabitsTracker.exceptions.VerificationTokenNotExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getField() + " " 
            + fieldError.getDefaultMessage() : "Validation failed";

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({PasswordFormatException.class, UsernameInUseException.class, 
        EmailInUseException.class, VerificationTokenNotExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUserRegister(RuntimeException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /*@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String message = "An unexpected error occurred";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), message);
        return ResponseEntity.internalServerError().body(errorResponse);
    }*/


    static class ErrorResponse {
        private String status;
        private String message;

        public ErrorResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }   
    }
}
