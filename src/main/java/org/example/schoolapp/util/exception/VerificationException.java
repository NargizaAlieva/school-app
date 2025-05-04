package org.example.schoolapp.util.exception;

public class VerificationException extends RuntimeException {
    public VerificationException() {
        super("Please verify your email");
    }
}
