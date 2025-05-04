package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.schoolapp.dto.request.LoginRequest;
import org.example.schoolapp.dto.request.RegisterRequest;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() throws IOException {

        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setFirstName("Test");
        validRegisterRequest.setLastName("User");
        validRegisterRequest.setPhone("+9999999999");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("Password123!");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@example.com");
        validLoginRequest.setPassword("Password123!");

        doNothing().when(emailService).sendVerificationEmail(anyString());
        doNothing().when(emailService).verifyTokenAndGenerateTokens(anyString(), any(HttpServletResponse.class));
    }

    @Test
    void register_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Please verify your email"));

        User savedUser = userRepository.findByEmail(validRegisterRequest.getEmail()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(validRegisterRequest.getFirstName(), savedUser.getFirstName());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest();
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setPassword("short");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturnError_WhenInvalidCredentials() throws Exception {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("nonexistent@example.com");
        invalidRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login failed")));
    }

    @Test
    void login_ShouldReturnError_WhenUserNotActive() throws Exception {
        userRepository.save(User.builder()
                .email(validLoginRequest.getEmail())
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .isActive(false)
                .build());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login failed")));
    }

    @Test
    void verify_ShouldCallEmailService_WhenTokenProvided() throws Exception {
        String testToken = "test-verification-token";

        mockMvc.perform(get("/api/v1/auth/verify")
                        .param("token", testToken))
                .andExpect(status().isOk());

        verify(emailService, times(1)).verifyTokenAndGenerateTokens(eq(testToken), any(HttpServletResponse.class));
    }

    @Test
    void getVerificationToken_ShouldCallEmailService_WhenEmailProvided() throws Exception {
        String testEmail = "test@example.com";

        mockMvc.perform(get("/api/v1/auth/get-verification-token")
                        .param("email", testEmail))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendVerificationEmail(eq(testEmail));
    }
}
