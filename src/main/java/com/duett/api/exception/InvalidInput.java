package com.duett.api.exception;


public class InvalidInput extends RuntimeException {

    public InvalidInput() {
        super();
    }

    public InvalidInput(String message) {
        super(message);
        
    }
}
