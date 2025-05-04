package org.example.schoolapp.service.entity.impl;

import java.util.Date;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.TokenRepository;
import org.example.schoolapp.service.entity.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Value("${token.expiration}")
    private long jwtExpiration;

    @Value("${verification-token.expiration}")
    private long verificationExpiration;

    @Value("${factor-auth-token.expiration}")
    private long factorAuthExpiration;

    @Value("${refresh-token.expiration}")
    private long refreshExpiration;

    @Override
    public Token getByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    @Override
    public void deleteByToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    @Override
    public void saveUserToken(User user, String jwtToken, TokenType tokenType) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .revoked(false)
                .createdAt(new Date(System.currentTimeMillis()))
                .expired(false)
                .build();

        if (TokenType.REFRESH.equals(tokenType))
            token.setExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration));
        else if (TokenType.VERIFICATION.equals(tokenType))
            token.setExpiresAt(new Date(System.currentTimeMillis() + verificationExpiration));
        else if (TokenType.BEARER.equals(tokenType))
            token.setExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration));
        else if (TokenType.FACTOR_AUTH.equals(tokenType))
            token.setExpiresAt(new Date(System.currentTimeMillis() + factorAuthExpiration));

        tokenRepository.save(token);
    }

    @Override
    public void setTokenCookies(HttpServletResponse response, JWTService jwtService, String accessToken, String refreshToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) jwtService.getJwtExpiration());
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) jwtService.getRefreshExpiration());
        response.addCookie(refreshTokenCookie);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validTokens);
        }
    }
}
