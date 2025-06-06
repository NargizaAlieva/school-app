package org.example.schoolapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.request.LoginRequest;
import org.example.schoolapp.dto.request.RegisterRequest;
import org.example.schoolapp.dto.response.AuthResponse;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.AuthProvider;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.service.entity.RoleService;
import org.example.schoolapp.service.entity.TokenService;
import org.example.schoolapp.service.entity.UserService;
import org.example.schoolapp.util.exception.NoTokenProvided;
import org.example.schoolapp.util.exception.VerificationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public void register(RegisterRequest request) {
        Set<Role> userRole = new HashSet<>();
        userRole.add(roleService.findByTitle("USER"));

        var user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName() == null ? "" : request.getMiddleName())
                .phone(request.getPhone() == null ? "" : request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleSet(userRole)
                .isEnabled(false)
                .provider(AuthProvider.LOCAL)
                .build();
        userService.createUser(user);

        emailService.sendVerificationEmail(user);
    }

    public Map<String, String> login(LoginRequest request) throws IOException {
        var user = userService.getEntityByEmail(request.getEmail());

        if (!user.getIsEnabled()) {
            throw new VerificationException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        if (user.getIs2FAEnabled()) {
            emailService.sendFactorAuthEmail(user);
            return new HashMap<>();
        } else
            return sendTokenResponse(user);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NoTokenProvided();
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            var userDetails = this.userService.getEntityByEmail(username);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                tokenService.revokeAllUserTokens(userDetails);
                var accessToken = jwtService.generateToken(userDetails);
                tokenService.saveUserToken(userDetails, accessToken, TokenType.BEARER);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public Map<String, String> sendTokenResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.saveUserToken(user, accessToken, TokenType.BEARER);
        tokenService.saveUserToken(user, refreshToken, TokenType.REFRESH);

        Map<String, String> tokens = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        return tokens;
    }
}
