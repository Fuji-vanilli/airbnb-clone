package com.fuji.airbnb_clone_backend.user.exception;

public class UserException extends RuntimeException{
    private String message;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
