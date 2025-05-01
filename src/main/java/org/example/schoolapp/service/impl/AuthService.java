package org.example.schoolapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.request.LoginRequest;
import org.example.schoolapp.dto.request.RegisterRequest;
import org.example.schoolapp.dto.response.AuthResponse;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.TokenRepository;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.RoleService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Set<Role> userRole = new HashSet<>();
        userRole.add(roleService.findByTitle("USER"));

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName() == null ? "" : request.getMiddleName())
                .phone(request.getPhone() == null ? "" : request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleSet(userRole)
                .build();
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);

        saveUserToken(user, accessToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.Bearer)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }
}
