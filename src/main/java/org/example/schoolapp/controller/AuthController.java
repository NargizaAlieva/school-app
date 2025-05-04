package org.example.schoolapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints related to user authentication and verification")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a user and sends an email for verification",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Registration failed", content = @Content(schema = @Schema(implementation = Response.class)))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
           authService.register(request);
            return ResponseEntity.ok("Please verify your email");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Registration failed: " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Login with credentials",
            description = "Authenticates a user and sends a 2FA email if successful",
            responses = {
                    @ApiResponse(responseCode = "200", description = "2FA email sent"),
                    @ApiResponse(responseCode = "500", description = "Login failed")
            }
    )
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

    @Operation(
            summary = "Verify user email",
            description = "Verifies the user's email using a token and generates access/refresh tokens",
            parameters = {
                    @Parameter(name = "token", description = "Verification token", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "User verified and tokens issued"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
            }
    )
    @GetMapping("/verify")
    public void verify(@RequestParam String token, HttpServletResponse response) throws IOException {
        emailService.verifyTokenAndGenerateTokens(token, response);
    }

    @Operation(
            summary = "Resend verification email",
            description = "Sends a new verification email to the specified address",
            parameters = {
                    @Parameter(name = "email", description = "Email to send the verification token to", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Verification email sent")
            }
    )
    @GetMapping("/get-verification-token")
    public void getVerificationToken(@RequestParam String email) {
        emailService.sendVerificationEmail(email);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token from a valid refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Access token refreshed"),
                    @ApiResponse(responseCode = "401", description = "Invalid or missing refresh token")
            }
    )
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}
