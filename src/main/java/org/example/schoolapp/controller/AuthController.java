package org.example.schoolapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.LoginRequest;
import org.example.schoolapp.dto.request.RegisterRequest;
import org.example.schoolapp.service.AuthService;
import org.example.schoolapp.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
           authService.register(request);
            return ResponseEntity.ok("Please verify your email");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Registration failed: " + exception.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authService.login(request);
            return ResponseEntity.ok("Please verify your 2 factor authentication email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/verify")
    public void verify(@RequestParam String token, HttpServletResponse response) throws IOException {
        emailService.verifyTokenAndGenerateTokens(token, response);
    }

    @GetMapping("/get-verification-token")
    public void getVerificationToken(@RequestParam String email) throws IOException {
        emailService.sendVerificationEmail(email);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}
