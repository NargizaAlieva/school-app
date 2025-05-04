package org.example.schoolapp.service.entity;

import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.TokenType;

public interface TokenService {
    Token getByToken(String token);

    void deleteByToken(String token);

    void saveUserToken(User user, String jwtToken, TokenType tokenType);

    void setTokenCookies(HttpServletResponse response, JWTService jwtService, String accessToken, String refreshToken);

    void revokeAllUserTokens(User user);
}
