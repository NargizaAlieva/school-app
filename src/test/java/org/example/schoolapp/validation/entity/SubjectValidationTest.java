package org.example.schoolapp.validation.entity;

import org.example.schoolapp.entity.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenSubjectIsValid_thenNoViolations() {
        Subject subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenTitleIsNull_thenTwoViolations() {
        Subject subject = Subject.builder()
                .title(null)
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Subject name cannot be null",
                "Subject title cannot be empty"
        );
    }

    @Test
    public void whenTitleIsBlank_thenOneViolation() {
        Subject subject = Subject.builder()
                .title("")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Subject title cannot be empty");
    }

    @Test
    public void whenTitleIsTooLong_thenOneViolation() {
        Subject subject = Subject.builder()
                .title("a".repeat(101))
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Subject title must be at most 100 characters");
    }

    @Test
    public void whenDescriptionIsTooLong_thenOneViolation() {
        Subject subject = Subject.builder()
                .title("Mathematics")
                .description("a".repeat(256))
                .isActive(true)
                .build();

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description must be at most 255 characters");
    }

    @Test
    public void whenIsActiveIsNull_thenNoViolation() {
        Subject subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(null)
                .build();

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);
        assertThat(violations).isEmpty();
    }
}