package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SubjectDtoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidSubjectDtoRequest() {
        SubjectDtoRequest request = SubjectDtoRequest.builder()
                .title("Mathematics")
                .description("Basic math concepts")
                .isActive(true)
                .build();

        Set<ConstraintViolation<SubjectDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullTitle() {
        SubjectDtoRequest request = new SubjectDtoRequest();
        request.setDescription("Science course");
        request.setIsActive(true);

        Set<ConstraintViolation<SubjectDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Subject name cannot be null"));
    }

    @Test
    void testTitleTooLong() {
        SubjectDtoRequest request = SubjectDtoRequest.builder()
                .title("A".repeat(101))
                .description("This is a description.")
                .isActive(true)
                .build();

        Set<ConstraintViolation<SubjectDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Subject title must be at most 100 characters"));
    }
}

