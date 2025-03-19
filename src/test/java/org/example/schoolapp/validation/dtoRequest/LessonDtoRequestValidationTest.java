package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LessonDtoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLessonDtoRequest() {
        LessonDtoRequest request = LessonDtoRequest.builder()
                .topic("Math Lesson")
                .scheduleId(1L)
                .build();

        Set<ConstraintViolation<LessonDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullTopic() {
        LessonDtoRequest request = new LessonDtoRequest();
        request.setScheduleId(1L);

        Set<ConstraintViolation<LessonDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Topic cannot be null"));
    }

    @Test
    void testBlankTopic() {
        LessonDtoRequest request = new LessonDtoRequest();
        request.setTopic(" ");
        request.setScheduleId(1L);

        Set<ConstraintViolation<LessonDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Topic cannot be blank"));
    }

    @Test
    void testNullScheduleId() {
        LessonDtoRequest request = new LessonDtoRequest();
        request.setTopic("Math Lesson");

        Set<ConstraintViolation<LessonDtoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("ScheduleId cannot be null"));
    }
}