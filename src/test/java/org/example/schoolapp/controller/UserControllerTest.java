package org.example.schoolapp.controller;

import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.entity.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void enable2FA_Success() throws Exception {
        ResponseEntity<Response> response = userController.enable2FA();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully enabled 2-factor authentication.", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(userService).enable2FA();
    }

    @Test
    void enable2FA_Failure() throws Exception {
        String errorMessage = "User not found";
        doThrow(new RuntimeException(errorMessage)).when(userService).enable2FA();

        ResponseEntity<Response> response = userController.enable2FA();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Couldn't found. " + errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void disable2FA_Success() throws Exception {
        ResponseEntity<Response> response = userController.disable2FA();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully disabled 2-factor authentication.", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(userService).disable2FA();
    }

    @Test
    void disable2FA_Failure() throws Exception {
        String errorMessage = "Database error";
        doThrow(new RuntimeException(errorMessage)).when(userService).disable2FA();

        ResponseEntity<Response> response = userController.disable2FA();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Couldn't found. " + errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void getProfile_Failure() throws Exception {
        String errorMessage = "Profile not available";
        when(userService.getById()).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Response> response = userController.getProfile();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Couldn't found. " + errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
