package com.example.moviepj.exception;

public class RoleNotFound extends RuntimeException {
    public RoleNotFound(String errorMes) {
        super(errorMes);
    }
}
