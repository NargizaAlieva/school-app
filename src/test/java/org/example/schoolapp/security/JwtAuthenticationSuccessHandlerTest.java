package org.example.schoolapp.security;

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

    private User enabledUser;
    private User disabledUser;

    @BeforeEach
    void setUp() {
        enabledUser = new User();
        enabledUser.setEmail("enabled@example.com");
        enabledUser.setIsEnabled(true);

        disabledUser = new User();
        disabledUser.setEmail("disabled@example.com");
        disabledUser.setIsEnabled(false);
    }

    @Test
    void onAuthenticationSuccess_WithEnabledUser_ShouldSend2FactorEmail() throws IOException {
        when(authentication.getPrincipal()).thenReturn(enabledUser);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(emailService).sendFactorAuthEmail(enabledUser);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertTrue(stringWriter.toString().contains("Please verify your 2-factor Authentication"));
    }

    @Test
    void onAuthenticationSuccess_WithDisabledUser_ShouldThrowVerificationException() throws IOException {
        when(authentication.getPrincipal()).thenReturn(disabledUser);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        assertThrows(VerificationException.class, () -> {
            successHandler.onAuthenticationSuccess(request, response, authentication);
        });

        verify(emailService, never()).sendFactorAuthEmail(any());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertTrue(stringWriter.toString().contains("Please first verify your email"));
    }

    @Test
    void onAuthenticationSuccess_WhenIOExceptionOccurs_ShouldPropagateException() throws IOException {
        when(authentication.getPrincipal()).thenReturn(enabledUser);
        when(response.getWriter()).thenThrow(new IOException("Failed to get writer"));

        assertThrows(IOException.class, () -> {
            successHandler.onAuthenticationSuccess(request, response, authentication);
        });
    }

    @Test
    void onAuthenticationSuccess_WhenPrincipalNotUser_ShouldThrowClassCastException() {
        when(authentication.getPrincipal()).thenReturn("not-a-user-object");

        assertThrows(ClassCastException.class, () -> {
            successHandler.onAuthenticationSuccess(request, response, authentication);
        });
    }
}