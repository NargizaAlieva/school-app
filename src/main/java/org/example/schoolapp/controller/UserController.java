package org.example.schoolapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.service.entity.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations related to user.")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Enable 2FA", description = "Enables 2-Factor Authentication for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully enabled 2-factor authentication"),
            @ApiResponse(responseCode = "404", description = "User not found or error occurred")
    })
    @GetMapping("/enable-2fa")
    public ResponseEntity<Response> enable2FA() {
        try {
            userService.enable2FA();
            return ResponseEntity.ok(new Response("Successfully enabled 2-factor authentication.", null));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Disable 2FA", description = "Disables 2-Factor Authentication for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully disabled 2-factor authentication"),
            @ApiResponse(responseCode = "404", description = "User not found or error occurred")
    })
    @GetMapping("/disable-2fa")
    public ResponseEntity<Response> disable2FA() {
        try {
            userService.disable2FA();
            return ResponseEntity.ok(new Response("Successfully disabled 2-factor authentication.", null));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Get current user's profile", description = "Receive current user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully receive your profile"),
            @ApiResponse(responseCode = "404", description = "User not found or error occurred")
    })
    @GetMapping("/get-profile")
    public ResponseEntity<Response> getProfile() {
        try {
            return ResponseEntity.ok(new Response("Successfully receive your profile.", userService.getById()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }
}
