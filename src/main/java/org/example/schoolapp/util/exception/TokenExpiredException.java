package org.example.schoolapp.util.exception;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException() {
        super("Your token has expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
