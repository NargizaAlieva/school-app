package org.example.schoolapp.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.AuthProvider;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.entity.TokenService;
import org.example.schoolapp.service.entity.UserService;
import org.example.schoolapp.util.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final TokenService tokenService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private String randomPassword;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String providerId = oauthToken.getAuthorizedClientRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(providerId.toUpperCase());

        String email = getEmailFromOAuthUser(oauthUser, provider);
        String name = getNameFromOAuthUser(oauthUser, provider);
        String[] nameParts = name != null ? name.split(" ", 2) : new String[]{"", ""};

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email, nameParts, provider));

        if (!user.isEnabled()) {
            throw new RuntimeException("Please first verify your email");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, accessToken, TokenType.BEARER);
        tokenService.saveUserToken(user, refreshToken, TokenType.REFRESH);

        tokenService.setTokenCookies(response, jwtService, accessToken, refreshToken);
        sendTokenResponse(response, accessToken, refreshToken);
    }

    private String getEmailFromOAuthUser(OAuth2User oauthUser, AuthProvider provider) {
        String email = oauthUser.getAttribute("email");

        if (email == null && provider == AuthProvider.GITHUB)
            email = oauthUser.getAttribute("login") + "@github.com";

        if (email == null)
            throw new IllegalStateException("Email not found from OAuth2 provider");

        return email;
    }

    private String getNameFromOAuthUser(OAuth2User oauthUser, AuthProvider provider) {
        String name = oauthUser.getAttribute("name");

        if (name == null && provider == AuthProvider.GITHUB)
            name = oauthUser.getAttribute("login");

        return name;
    }

    private User createNewUser(String email, String[] nameParts, AuthProvider provider) {
        randomPassword = UUID.randomUUID().toString();
        User newUser = User.builder()
                .email(email)
                .firstName(nameParts[0])
                .lastName(nameParts.length > 1 ? nameParts[1] : "")
                .provider(provider)
                .password(passwordEncoder.encode(randomPassword))
                .isEnabled(true)
                .roleSet(new HashSet<>())
                .build();
        newUser = userService.createUser(newUser);
        Role role = roleRepository.findByTitle("USER").orElseThrow();
        newUser.getRoleSet().add(role);
        return newUser;
    }

    private void sendTokenResponse(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException, java.io.IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"accessToken\":\"%s\", \"refreshToken\":\"%s\", \"randomPassword\":\"%s\"}",
                accessToken,
                refreshToken,
                randomPassword
        ));
    }
}