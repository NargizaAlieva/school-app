package org.example.schoolapp.util.exception;

public class NoTokenProvided extends RuntimeException {
    public NoTokenProvided() {
        super("No token provided");
    }
}
