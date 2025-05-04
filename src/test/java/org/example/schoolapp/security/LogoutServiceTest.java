package org.example.schoolapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.config.LogoutService;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService logoutService;

    private final String validToken = "valid.jwt.token";
    private final String invalidToken = "invalid.jwt.token";
    private Token storedToken;

    @BeforeEach
    void setUp() {
        storedToken = new Token();
        storedToken.setToken(validToken);
        storedToken.setExpired(false);
        storedToken.setRevoked(false);
    }

    @Test
    void logout_WithValidBearerToken_ShouldInvalidateToken() {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(tokenRepository.findByToken(validToken)).thenReturn(Optional.of(storedToken));

        logoutService.logout(request, response, authentication);

        verify(tokenRepository).findByToken(validToken);
        verify(tokenRepository).save(storedToken);
        assertTrue(storedToken.isExpired());
        assertTrue(storedToken.isRevoked());
    }

    @Test
    void logout_WithNoAuthorizationHeader_ShouldDoNothing() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(any());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void logout_WithInvalidAuthorizationHeader_ShouldDoNothing() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(any());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void logout_WithNonBearerToken_ShouldDoNothing() {
        when(request.getHeader("Authorization")).thenReturn("Basic " + validToken);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(any());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void logout_WithUnknownToken_ShouldDoNothing() {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(tokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository).findByToken(invalidToken);
        verify(tokenRepository, never()).save(any());
    }
}
