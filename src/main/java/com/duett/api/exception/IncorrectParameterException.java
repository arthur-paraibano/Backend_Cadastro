package com.duett.api.exception;


public class IncorrectParameterException extends RuntimeException {

    public IncorrectParameterException() {
        super();
    }

    public IncorrectParameterException(String message) {
        super(message);
        
    }
}
