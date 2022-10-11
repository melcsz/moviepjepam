package com.example.moviepj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class EmailOrPasswordDoNotMatch extends RuntimeException{

    private static final long serialVersionUID = 1L;

}
