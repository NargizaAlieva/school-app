package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class StudentDtoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidStudentDtoRequest() {
        StudentDtoRequest request = StudentDtoRequest.builder()
                .parentStatus("Active")
                .userId(1L)
                .parentId(2L)
                .gradeId(3L)
                .build();

        Set<ConstraintViolation<StudentDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullParentStatus() {
        StudentDtoRequest request = new StudentDtoRequest();
        request.setUserId(1L);
        request.setParentId(2L);
        request.setGradeId(3L);

        Set<ConstraintViolation<StudentDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Parent status cannot be null"));
    }
}

