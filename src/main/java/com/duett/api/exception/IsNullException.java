package com.duett.api.exception;


public class IsNullException extends RuntimeException {

    public IsNullException() {
        super();
    }

    public IsNullException(String message) {
        super(message);
        
    }
}
