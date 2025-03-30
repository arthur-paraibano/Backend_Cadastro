package com.duett.api.exception;


public class InternalServer extends RuntimeException {

    public InternalServer() {
        super();
    }

    public InternalServer(String message) {
        super(message);
        
    }
}
