package com.moger.crudproject.exception;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException() {
        super("No data found");
    }
}
