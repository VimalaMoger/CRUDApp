package com.moger.crudproject.exception;

public class StudentNotFoundException extends RuntimeException{

    public StudentNotFoundException(Long id) {
        super(String.format("Student with Id %d not found", id));
    }
}
