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

}
