package org.example.schoolapp.util.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleObjectNotFoundException() {
        ObjectNotFoundException exception = new ObjectNotFoundException("Entity not found");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleObjectNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Entity not found", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void testHandleAlreadyExistException() {
        AlreadyExistException exception = new AlreadyExistException("Entity already exists");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Entity already exists", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Request: Invalid argument", Objects.requireNonNull(response.getBody()).get("message"));
        assertEquals("Bad Request", response.getBody().get("error"));
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Database constraint violation");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDataIntegrityViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Database Integrity Error: Database constraint violation", Objects.requireNonNull(response.getBody()).get("message"));
        assertEquals("Bad Request", response.getBody().get("error"));
    }
}