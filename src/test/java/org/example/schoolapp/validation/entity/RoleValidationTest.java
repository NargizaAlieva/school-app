package org.example.schoolapp.validation.entity;

import org.example.schoolapp.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenRoleIsValid_thenNoViolations() {
        Role role = Role.builder()
                .title("ROLE_ADMIN")
                .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenTitleIsNull_thenTwoViolations() {
        Role role = Role.builder()
                .title(null)
                .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Role title cannot be null",
                "Role title cannot be empty"
        );
    }

    @Test
    public void whenTitleIsBlank_thenTwoViolations() {
        Role role = Role.builder()
                .title("")
                .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Role title cannot be empty",
                "Role title must be between 1 and 50 characters"
        );
    }

    @Test
    public void whenTitleIsTooShort_thenOneViolation() {
        Role role = Role.builder()
                .title("")
                .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Role title cannot be empty",
                "Role title must be between 1 and 50 characters"
        );
    }

    @Test
    public void whenTitleIsTooLong_thenOneViolation() {
        Role role = Role.builder()
                .title("a".repeat(51))
                .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Role title must be between 1 and 50 characters");
    }
}