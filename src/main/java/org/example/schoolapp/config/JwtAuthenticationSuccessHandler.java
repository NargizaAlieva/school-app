package org.example.schoolapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.EmailService;
import org.example.schoolapp.util.exception.VerificationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final EmailService emailService;

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        if (!user.getIsEnabled()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), "Please first verify your email");

            throw new VerificationException();
        }

        emailService.sendFactorAuthEmail(user);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), "Please verify your 2-factor Authentication. We sent you email message");
    }
}
