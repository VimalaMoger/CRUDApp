package com.moger.crudproject.exception;

public class MethodArgumentNotValidException extends RuntimeException {

    public MethodArgumentNotValidException(String message){
        super(message);
    }
}

