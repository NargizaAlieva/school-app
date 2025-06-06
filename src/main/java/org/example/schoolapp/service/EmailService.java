package org.example.schoolapp.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.entity.TokenService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.exception.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final TokenService tokenService;
    private final JavaMailSender mailSender;

    @Value("${base-url}")
    private String baseUrl;

    public void sendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email: " + email + "not faund"));

        sendVerificationEmail(user);
    }

    public void sendVerificationEmail(User user) {
        String verificationToken =  UUID.randomUUID().toString();
        tokenService.saveUserToken(user, verificationToken, TokenType.VERIFICATION);
        String link = baseUrl + "auth/verify?token=" + verificationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify your email address");
        message.setText("Please click the following link to verify your email: " + link);

        mailSender.send(message);
    }

    public void sendFactorAuthEmail(User user) {
        String factorAuth = UUID.randomUUID().toString();
        tokenService.saveUserToken(user, factorAuth, TokenType.FACTOR_AUTH);
        String link = baseUrl + "auth/verify?token=" + factorAuth;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your 2FA Verification Code");
        message.setText("Please click the following link to verify your email: " + link);


        mailSender.send(message);
    }

    public void verifyTokenAndGenerateTokens(String token, HttpServletResponse response) throws IOException {
        Token verificationToken = tokenService.getByToken(token);

        if (verificationToken.isExpired() || verificationToken.isRevoked())
            throw new TokenExpiredException("Token expired. You can get new Verification token by this link: " + baseUrl + "auth/get-verification-token/" + "your email");

        User user = verificationToken.getUser();
        user.setIsEnabled(true);
        userRepository.save(user);

        generateTokens(response, user);
    }

    public void generateTokens(HttpServletResponse response, User user) throws IOException {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.saveUserToken(user, accessToken, TokenType.BEARER);
        tokenService.saveUserToken(user, refreshToken, TokenType.REFRESH);

        tokenService.setTokenCookies(response, jwtService, accessToken, refreshToken);

        sendTokenResponse(response, accessToken, refreshToken);
    }

    public void sendTokenResponse(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"accessToken\":\"%s\", \"refreshToken\":\"%s\"}",
                accessToken,
                refreshToken
        ));
    }
}
