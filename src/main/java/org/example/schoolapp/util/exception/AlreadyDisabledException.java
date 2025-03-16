package org.example.schoolapp.util.exception;

public class AlreadyDisabledException extends RuntimeException {
    public AlreadyDisabledException(String object, Long id) {
        super(object + " with Id " + id + " is already disabled.");
    }
}
