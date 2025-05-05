package org.example.schoolapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.dto.request.LoginRequest;
import org.example.schoolapp.dto.request.RegisterRequest;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.AuthService;
import org.example.schoolapp.service.EmailService;
import org.example.schoolapp.service.entity.RoleService;
import org.example.schoolapp.service.entity.UserService;
import org.example.schoolapp.util.exception.NoTokenProvided;
import org.example.schoolapp.util.exception.VerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserService userService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleService roleService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private EmailService emailService;

    @InjectMocks private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldCreateUserAndSendEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .email("email@test.com")
                .password("pass")
                .firstName("John")
                .lastName("Doe")
                .build();

        Role role = Role.builder().build();

        when(roleService.findByTitle("USER")).thenReturn(role);
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        authService.register(request);

        verify(emailService).sendVerificationEmail(any(User.class));
    }

    @Test
    void login_shouldAuthenticateAndSend2FA() throws IOException {
        LoginRequest request = LoginRequest.builder()
                .email("email@test.com")
                .password("password")
                .build();

        User user = User.builder().isEnabled(true).build();

        when(userService.getEntityByEmail("email@test.com")).thenReturn(user);

        authService.login(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(emailService).sendFactorAuthEmail(user);
    }

    @Test
    void login_shouldThrowVerificationException_ifUserNotEnabled() {
        LoginRequest request = LoginRequest.builder()
                .email("email@test.com")
                .password("password")
                .build();

        User user = User.builder().isEnabled(false).build();

        when(userService.getEntityByEmail("email@test.com")).thenReturn(user);

        assertThrows(VerificationException.class, () -> authService.login(request));
    }

    @Test
    void refreshToken_shouldThrowNoTokenProvided_ifNoHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(NoTokenProvided.class, () -> authService.refreshToken(request, response));
    }
}
