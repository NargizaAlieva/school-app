package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GradeDtoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidGradeDtoRequest() {
        GradeDtoRequest request = GradeDtoRequest.builder()
                .title("Grade A")
                .classTeacherId(1L)
                .build();

        Set<ConstraintViolation<GradeDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullTitle() {
        GradeDtoRequest request = new GradeDtoRequest();
        request.setClassTeacherId(1L);

        Set<ConstraintViolation<GradeDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Title cannot be null"));
    }

    @Test
    void testBlankTitle() {
        GradeDtoRequest request = new GradeDtoRequest();
        request.setTitle(" ");
        request.setClassTeacherId(1L);

        Set<ConstraintViolation<GradeDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Title cannot be blank"));
    }

    @Test
    void testNullClassTeacherId() {
        GradeDtoRequest request = new GradeDtoRequest();
        request.setTitle("Grade A");

        Set<ConstraintViolation<GradeDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Class TeacherId cannot be null"));
    }
}
