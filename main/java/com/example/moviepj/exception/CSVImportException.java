package com.example.moviepj.exception;

public class CSVImportException extends RuntimeException{
    public CSVImportException(String errorMessage){
        super(errorMessage);
    }
}
