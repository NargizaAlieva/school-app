package org.example.schoolapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.dto.request.LoginRequest;
import org.example.schoolapp.dto.request.RegisterRequest;
import org.example.schoolapp.service.AuthService;
import org.example.schoolapp.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private EmailService emailService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("test");
        registerRequest.setLastName("test");

        loginRequest = new LoginRequest(
                "test@example.com",
                "password"
        );
    }

    @Test
    void register_Success() {
        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Please verify your email", response.getBody());
        verify(authService).register(registerRequest);
    }

    @Test
    void register_Failure() {
        String errorMessage = "Email already exists";
        doThrow(new RuntimeException(errorMessage)).when(authService).register(registerRequest);

        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(errorMessage));
    }

    @Test
    void login_SuccessWithTokens() throws IOException {
        Map<String, String> tokens = Map.of("accessToken", "token123", "refreshToken", "refresh123");
        when(authService.login(any(LoginRequest.class))).thenReturn(tokens);

        ResponseEntity<?> result = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) result.getBody();
        assertEquals("This is you tokens", responseBody.get("message"));
        assertEquals(tokens, responseBody.get("tokens"));
    }

    @Test
    void login_Failure() throws IOException {
        String errorMessage = "Invalid credentials";
        doThrow(new RuntimeException(errorMessage)).when(authService).login(loginRequest);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(errorMessage));
    }

    @Test
    void verify_Success() throws IOException {
        String token = "test-token";

        authController.verify(token, response);

        verify(emailService).verifyTokenAndGenerateTokens(token, response);
    }

    @Test
    void getVerificationToken_Success() throws IOException {
        String email = "test@example.com";

        authController.getVerificationToken(email);

        verify(emailService).sendVerificationEmail(email);
    }

    @Test
    void refreshToken_Success() throws IOException {
        authController.refreshToken(request, response);

        verify(authService).refreshToken(request, response);
    }

    @Test
    void verify_IOException() throws IOException {
        String token = "test-token";
        doThrow(new IOException("Token verification failed")).when(emailService).verifyTokenAndGenerateTokens(token, response);

        assertThrows(IOException.class, () -> authController.verify(token, response));
    }

    @Test
    void refreshToken_IOException() throws IOException {
        doThrow(new IOException("Token refresh failed")).when(authService).refreshToken(request, response);

        assertThrows(IOException.class, () -> authController.refreshToken(request, response));
    }
}
