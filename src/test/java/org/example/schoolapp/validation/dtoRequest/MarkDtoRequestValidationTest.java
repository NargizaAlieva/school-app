package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MarkDtoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMarkDtoRequest() {
        MarkDtoRequest request = MarkDtoRequest.builder()
                .mark(85)
                .studentId(1L)
                .lessonId(1L)
                .build();

        Set<ConstraintViolation<MarkDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullMark() {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setStudentId(1L);
        request.setLessonId(1L);

        Set<ConstraintViolation<MarkDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Mark cannot be null"));
    }

    @Test
    void testMarkBelowMinimum() {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setMark(0);
        request.setStudentId(1L);
        request.setLessonId(1L);

        Set<ConstraintViolation<MarkDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Mark must be at least 1"));
    }

    @Test
    void testMarkAboveMaximum() {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setMark(101);
        request.setStudentId(1L);
        request.setLessonId(1L);

        Set<ConstraintViolation<MarkDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Mark cannot exceed 100"));
    }

    @Test
    void testNullStudentId() {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setMark(50);
        request.setLessonId(1L);

        Set<ConstraintViolation<MarkDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("StudentId cannot be null"));
    }

    @Test
    void testNullLessonId() {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setMark(50);
        request.setStudentId(1L);

        Set<ConstraintViolation<MarkDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("LessonId cannot be null"));
    }
}
