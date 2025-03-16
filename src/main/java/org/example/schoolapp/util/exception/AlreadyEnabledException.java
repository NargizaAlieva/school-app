package org.example.schoolapp.util.exception;

public class AlreadyEnabledException extends RuntimeException {
    public AlreadyEnabledException(String object, Long id) {
        super(object + " with Id " + id + " is already active.");
    }
}
