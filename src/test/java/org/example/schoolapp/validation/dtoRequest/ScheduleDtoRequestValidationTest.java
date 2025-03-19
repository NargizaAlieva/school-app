package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleDtoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidScheduleDtoRequest() {
        ScheduleDtoRequest request = ScheduleDtoRequest.builder()
                .dayOfWeek("Monday")
                .quarter(1)
                .dueTime("08:00-09:00")
                .schoolYear("2024-2025")
                .subjectId(1L)
                .teacherId(1L)
                .gradeId(1L)
                .isApprove(true)
                .isActive(true)
                .build();

        Set<ConstraintViolation<ScheduleDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullDayOfWeek() {
        ScheduleDtoRequest request = new ScheduleDtoRequest();
        request.setQuarter(1);
        request.setDueTime("08:00-09:00");
        request.setSchoolYear("2024-2025");
        request.setSubjectId(1L);
        request.setTeacherId(1L);
        request.setGradeId(1L);

        Set<ConstraintViolation<ScheduleDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Day of the week cannot be null"));
    }
}

