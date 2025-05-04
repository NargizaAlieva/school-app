package org.example.schoolapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.schoolapp.config.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JWTService jwtService;

    // 512-bit (64-byte) secret key for HS512
    private final String secretKey = Base64.getEncoder().encodeToString(
            Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

    private final long expiration = 86400000; // 1 day in milliseconds
    private Key signingKey;

    @BeforeEach
    void setUp() {
        // Generate a proper signing key
        signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

        jwtService.setSecretKey(secretKey);
        jwtService.setJwtExpiration(expiration);
        jwtService.setRefreshExpiration(expiration * 7); // 7 days
        jwtService.setVerificationExpiration(expiration / 24); // 1 hour

        when(userDetails.getUsername()).thenReturn("testuser@example.com");
    }

    @Test
    void extractUsername_ShouldReturnUsername_WhenTokenIsValid() {
        // Given
        String token = generateTestToken(userDetails, new HashMap<>(), expiration);

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertEquals("testuser@example.com", username);
    }

    @Test
    void generateToken_ShouldReturnValidToken_WithUserDetails() {
        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser@example.com", jwtService.extractUsername(token));
    }

    @Test
    void generateToken_ShouldReturnValidToken_WithExtraClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123);

        // When
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Then
        assertNotNull(token);
        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("ADMIN", claims.get("role"));
        assertEquals(123, claims.get("userId"));
    }

    @Test
    void generateRefreshToken_ShouldReturnLongLivedToken() {
        // When
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Then
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(jwtService.extractExpiration(refreshToken).after(
                new Date(System.currentTimeMillis() + expiration * 6))); // Should be >6 days
    }

    @Test
    void isTokenValid_ShouldReturnTrue_WhenTokenMatchesUserDetails() {
        // Given
        String token = generateTestToken(userDetails, new HashMap<>(), expiration);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenUsernameDoesNotMatch() {
        // Given
        String token = generateTestToken(userDetails, new HashMap<>(), expiration);
        UserDetails otherUser = mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("otheruser@example.com");

        // When
        boolean isValid = jwtService.isTokenValid(token, otherUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_ShouldReturnFalse_WhenTokenIsNotExpired() {
        // Given
        String validToken = generateTestToken(userDetails, new HashMap<>(), expiration);

        // When
        boolean isExpired = jwtService.isTokenExpired(validToken);

        // Then
        assertFalse(isExpired);
    }

    @Test
    void extractClaim_ShouldReturnClaimValue_WhenTokenIsValid() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("customClaim", "customValue");
        String token = generateTestToken(userDetails, claims, expiration);

        // When
        String customClaim = jwtService.extractClaim(token, c -> c.get("customClaim", String.class));

        // Then
        assertEquals("customValue", customClaim);
    }

    private String generateTestToken(UserDetails userDetails, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }
}