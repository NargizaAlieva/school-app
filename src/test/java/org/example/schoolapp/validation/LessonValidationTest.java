package org.example.schoolapp.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.entity.Lesson;
import org.example.schoolapp.entity.Schedule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LessonValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenLessonIsValid_thenNoViolations() {
        Schedule schedule = new Schedule();
        Lesson lesson = Lesson.builder()
                .topic("Mathematics")
                .homework("Complete exercises 1-10")
                .schedule(schedule)
                .build();

        Set<ConstraintViolation<Lesson>> violations = validator.validate(lesson);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenTopicIsNull_thenTwoViolations() {
        Schedule schedule = new Schedule();
        Lesson lesson = Lesson.builder()
                .topic(null)
                .homework("Complete exercises 1-10")
                .schedule(schedule)
                .build();

        Set<ConstraintViolation<Lesson>> violations = validator.validate(lesson);
        assertThat(violations).hasSize(2);

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(messages).containsExactlyInAnyOrder(
                "Topic cannot be null",
                "Topic cannot be blank"
        );
    }

    @Test
    void whenTopicIsBlank_thenOneViolation() {
        Schedule schedule = new Schedule();
        Lesson lesson = Lesson.builder()
                .topic("")
                .homework("Complete exercises 1-10")
                .schedule(schedule)
                .build();

        Set<ConstraintViolation<Lesson>> violations = validator.validate(lesson);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Topic cannot be blank");
    }

    @Test
    void whenScheduleIsNull_thenOneViolation() {
        Lesson lesson = Lesson.builder()
                .topic("Mathematics")
                .homework("Complete exercises 1-10")
                .schedule(null)
                .build();

        Set<ConstraintViolation<Lesson>> violations = validator.validate(lesson);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Schedule cannot be null");
    }
}