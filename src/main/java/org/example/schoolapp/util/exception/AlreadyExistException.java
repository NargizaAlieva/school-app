package org.example.schoolapp.util.exception;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String object, String parameterType, String parameter) {
        super(object + " with that " + parameterType + ": '" + parameter + "' is already exist.");
    }

    public AlreadyExistException(String object, String parameterType, Long parameter) {
        super(object + " with that " + parameterType + ": '" + parameter + "' is already exist.");
    }
}
