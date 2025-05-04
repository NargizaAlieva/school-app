package org.example.schoolapp.validation.entity;

import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenUserIsValid_thenNoViolations() {
        User user = User.builder()
                .username("username")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    public void whenUsernameIsNull_thenTwoViolations() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Username cannot be null",
                "Username cannot be empty"
        );
    }

    @Test
    public void whenUsernameIsBlank_thenTwoViolations() {
        User user = User.builder()
                .username("")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Username cannot be empty",
                "Username must be between 3 and 50 characters"
        );
    }

    @Test
    public void whenUsernameIsTooShort_thenOneViolation() {
        User user = User.builder()
                .username("me")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Username must be between 3 and 50 characters");
    }

    @Test
    public void whenEmailIsInvalid_thenOneViolation() {
        User user = User.builder()
                .username("JohnDoe")
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .password("password123")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Invalid email format");
    }

    @Test
    public void whenPasswordIsTooShort_thenOneViolation() {
        User user = User.builder()
                .username("JohnDoe")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("short")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password must be at least 8 characters long");
    }
}