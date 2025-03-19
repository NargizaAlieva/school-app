package org.example.schoolapp.validation.entity;

import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ParentValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenParentIsValid_thenNoViolations() {
        User user = User.builder()
                .username("parentuser")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password123")
                .build();

        Parent parent = Parent.builder()
                .user(user)
                .build();

        Set<ConstraintViolation<Parent>> violations = validator.validate(parent);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenUserIsNull_thenOneViolation() {
        Parent parent = Parent.builder()
                .user(null)
                .build();

        Set<ConstraintViolation<Parent>> violations = validator.validate(parent);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("User cannot be null");
    }

    @Test
    public void whenUserIsInvalid_thenTwoViolations() {
        User user = User.builder()
                .username(null)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password123")
                .build();

        Parent parent = Parent.builder()
                .user(user)
                .build();

        Set<ConstraintViolation<Parent>> violations = validator.validate(parent);

        assertThat(violations).hasSize(2);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(messages).containsExactlyInAnyOrder(
                "Username cannot be null",
                "Username cannot be empty"
        );
    }
}