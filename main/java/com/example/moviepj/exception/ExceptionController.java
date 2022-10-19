package com.example.moviepj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController extends RuntimeException{

    @ExceptionHandler(value = EmailOrPasswordDoNotMatch.class)
    public ResponseEntity<Object> exception(EmailOrPasswordDoNotMatch exception) {
        return new ResponseEntity<>("Wrong email or password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PasswordsDoNotMatch.class)
    public ResponseEntity<Object> exception(PasswordsDoNotMatch exception) {
        return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmailAlreadyInUseException.class)
    public ResponseEntity<Object> exception(EmailAlreadyInUseException exception) {
        return new ResponseEntity<>("Email is already in use", HttpStatus.BAD_REQUEST);
    }
}
