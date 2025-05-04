package org.example.schoolapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.config.JWTAuthenticationFilter;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private final String testToken = "test.jwt.token";
    private final String testUserEmail = "test@example.com";
    private UserDetails testUserDetails;

    @BeforeEach
    void setUp() {
        testUserDetails = new User(testUserEmail, "password", Collections.emptyList());
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoAuthorizationHeaderAndNoCookie_ShouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_ValidAuthorizationHeader_ShouldAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken);
        when(jwtService.extractUsername(testToken)).thenReturn(testUserEmail);
        when(userDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(testToken, testUserDetails)).thenReturn(true);
        when(tokenRepository.findByToken(testToken))
                .thenReturn(Optional.of(new Token()));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(testUserEmail, ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @Test
    void doFilterInternal_ValidCookieToken_ShouldAuthenticate() throws ServletException, IOException {
        Cookie[] cookies = {new Cookie("accessToken", testToken)};
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(cookies);
        when(jwtService.extractUsername(testToken)).thenReturn(testUserEmail);
        when(userDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(testToken, testUserDetails)).thenReturn(true);
        when(tokenRepository.findByToken(testToken))
                .thenReturn(Optional.of(new Token()));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_InvalidToken_ShouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken);
        when(jwtService.extractUsername(testToken)).thenReturn(testUserEmail);
        when(userDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(testToken, testUserDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_ExpiredToken_ShouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken);
        when(jwtService.extractUsername(testToken)).thenReturn(testUserEmail);
        when(userDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(testToken, testUserDetails)).thenReturn(true);

        Token expiredToken = new Token();
        expiredToken.setExpired(true);
        when(tokenRepository.findByToken(testToken)).thenReturn(Optional.of(expiredToken));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_RevokedToken_ShouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken);
        when(jwtService.extractUsername(testToken)).thenReturn(testUserEmail);
        when(userDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(testToken, testUserDetails)).thenReturn(true);

        Token revokedToken = new Token();
        revokedToken.setRevoked(true);
        when(tokenRepository.findByToken(testToken)).thenReturn(Optional.of(revokedToken));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_NoTokenInDB_ShouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken);
        when(jwtService.extractUsername(testToken)).thenReturn(testUserEmail);
        when(userDetailsService.loadUserByUsername(testUserEmail)).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(testToken, testUserDetails)).thenReturn(true);
        when(tokenRepository.findByToken(testToken)).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
