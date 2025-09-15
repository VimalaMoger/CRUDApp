package com.moger.crudproject.exception;

	public class DataAlreadyExistsException extends RuntimeException {

	    public DataAlreadyExistsException(String message) {
	        super(message);
	    }
	}