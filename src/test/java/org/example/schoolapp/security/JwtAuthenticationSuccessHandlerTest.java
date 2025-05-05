package org.example.schoolapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.config.JwtAuthenticationSuccessHandler;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.EmailService;
import org.example.schoolapp.util.exception.VerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationSuccessHandlerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtAuthenticationSuccessHandler successHandler;

    private User user;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        user = new User();
        user.setEmail("test@example.com");
        user.setIsEnabled(true);
        user.setIs2FAEnabled(false);

        when(authentication.getPrincipal()).thenReturn(user);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void onAuthenticationSuccess_WhenUserNotEnabled_ThrowsVerificationException() throws IOException {
        user.setIsEnabled(false);

        assertThrows(VerificationException.class, () -> {
            successHandler.onAuthenticationSuccess(request, response, authentication);
        });

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertEquals("\"Please first verify your email\"", responseWriter.toString());
    }

    @Test
    void onAuthenticationSuccess_When2FAEnabled_Sends2FAEmail() throws IOException {
        user.setIs2FAEnabled(true);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(emailService).sendFactorAuthEmail(user);
        verify(emailService, never()).generateTokens(any(), any());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertEquals("\"Please verify your 2-factor Authentication. We sent you email message\"", responseWriter.toString());
    }

    @Test
    void onAuthenticationSuccess_When2FADisabled_GeneratesTokens() throws IOException {
        user.setIs2FAEnabled(false);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(emailService, never()).sendFactorAuthEmail(any());
        verify(emailService).generateTokens(response, user);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertEquals("\"Please verify your 2-factor Authentication. We sent you email message\"", responseWriter.toString());
    }

    @Test
    void onAuthenticationSuccess_WhenIOExceptionOccurs_ThrowsRuntimeException() throws IOException {
        when(response.getWriter()).thenThrow(new IOException("Test IO Exception"));

        assertThrows(IOException.class, () -> {
            successHandler.onAuthenticationSuccess(request, response, authentication);
        });
    }
}