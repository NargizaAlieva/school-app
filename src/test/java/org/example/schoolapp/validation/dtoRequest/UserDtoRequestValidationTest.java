package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDto() {
        UserDtoRequest user = UserDtoRequest.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .middleName("A")
                .phone("+1234567890")
                .email("john.doe@example.com")
                .password("securePass1")
                .isActive(true)
                .build();

        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "UserDtoRequest should be valid");
    }

    @Test
    void testInvalidUsername() {
        UserDtoRequest user = UserDtoRequest.builder()
                .username("a")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("securePass1")
                .build();

        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Username validation should fail");
    }

    @Test
    void testInvalidEmail() {
        UserDtoRequest user = UserDtoRequest.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .password("securePass1")
                .build();

        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Email validation should fail");
    }

    @Test
    void testShortPassword() {
        UserDtoRequest user = UserDtoRequest.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("123")
                .build();

        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Password validation should fail");
    }

    @Test
    void testInvalidPhone() {
        UserDtoRequest user = UserDtoRequest.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("securePass1")
                .phone("123abc")
                .build();

        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Phone number validation should fail");
    }
}