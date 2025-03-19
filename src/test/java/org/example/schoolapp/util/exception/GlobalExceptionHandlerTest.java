package org.example.schoolapp.util.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationException_ShouldReturnBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("object", "field", "must not be null")));

        MethodArgumentNotValidException mockException = mock(MethodArgumentNotValidException.class);
        when(mockException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleValidationException(mockException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().get("error"));
    }

    @Test
    void handleObjectNotFoundException_ShouldReturnNotFound() {
        ObjectNotFoundException exception = new ObjectNotFoundException("Resource not found");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleObjectNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().get("message"));
    }

    @Test
    void handleAlreadyExistsException_ShouldReturnConflict() {
        AlreadyExistException exception = new AlreadyExistException("Resource already exists");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource already exists", response.getBody().get("message"));
    }

    @Test
    void handleAlreadyDisabledException_ShouldReturnBadRequest() {
        AlreadyDisabledException exception = new AlreadyDisabledException("Resource already disabled", 1L);
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAlreadyDisabledException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource already disabled with Id 1 is already disabled.", response.getBody().get("message"));
    }

    @Test
    void handleAlreadyEnabledException_ShouldReturnBadRequest() {
        AlreadyEnabledException exception = new AlreadyEnabledException("Resource already enabled", 1L);
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAlreadyEnabledException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource already enabled with Id 1 is already active.", response.getBody().get("message"));
    }

    @Test
    void handleIncorrectRequestException_ShouldReturnBadRequest() {
        IncorrectRequestException exception = new IncorrectRequestException("username");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleIncorrectRequestException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("You need to provide username", response.getBody().get("message"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().get("message"));
    }

    @Test
    void handleConstraintViolationException_ShouldReturnBadRequest() {
        ConstraintViolationException exception = new ConstraintViolationException("Validation failed", Set.of());
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleConstraintViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().get("error"));
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().get("error"));
    }
}