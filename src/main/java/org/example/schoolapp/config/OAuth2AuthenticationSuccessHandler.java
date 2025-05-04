package org.example.schoolapp.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.Token;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.AuthProvider;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.repository.TokenRepository;
import org.example.schoolapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
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
        String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email, username, nameParts, provider));

        revokeAllUserTokens(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, accessToken, TokenType.BEARER);
        saveUserToken(user, refreshToken, TokenType.REFRESH);

        setTokenCookies(response, accessToken, refreshToken);
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

    private User createNewUser(String email, String username, String[] nameParts, AuthProvider provider) {
        randomPassword = UUID.randomUUID().toString();
        User newUser = User.builder()
                .email(email)
                .firstName(nameParts[0])
                .lastName(nameParts.length > 1 ? nameParts[1] : "")
                .provider(provider)
                .password(passwordEncoder.encode(randomPassword))
                .roleSet(new HashSet<>())
                .build();
        newUser = userRepository.save(newUser);
        Role role = roleRepository.findByTitle("USER").orElseThrow();
        newUser.getRoleSet().add(role);
        return newUser;
    }

    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
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

    private void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validTokens);
        }
    }

    private void saveUserToken(User user, String jwtToken, TokenType tokenType) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}