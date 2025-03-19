package org.example.schoolapp.validation.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.entity.Mark;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.Lesson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MarkValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenMarkIsValid_thenNoViolations() {
        Student student = new Student();
        Lesson lesson = new Lesson();
        Mark mark = Mark.builder()
                .mark(85)
                .studentMark(student)
                .lessonMark(lesson)
                .build();

        Set<ConstraintViolation<Mark>> violations = validator.validate(mark);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenMarkIsNull_thenOneViolation() {
        Student student = new Student();
        Lesson lesson = new Lesson();
        Mark mark = Mark.builder()
                .mark(null)
                .studentMark(student)
                .lessonMark(lesson)
                .build();

        Set<ConstraintViolation<Mark>> violations = validator.validate(mark);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Mark cannot be null");
    }

    @Test
    void whenMarkIsLessThan1_thenOneViolation() {
        Student student = new Student();
        Lesson lesson = new Lesson();
        Mark mark = Mark.builder()
                .mark(0)
                .studentMark(student)
                .lessonMark(lesson)
                .build();

        Set<ConstraintViolation<Mark>> violations = validator.validate(mark);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Mark must be at least 1");
    }

    @Test
    void whenMarkIsGreaterThan100_thenOneViolation() {
        Student student = new Student();
        Lesson lesson = new Lesson();
        Mark mark = Mark.builder()
                .mark(101)
                .studentMark(student)
                .lessonMark(lesson)
                .build();

        Set<ConstraintViolation<Mark>> violations = validator.validate(mark);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Mark cannot exceed 100");
    }

    @Test
    void whenStudentMarkIsNull_thenOneViolation() {
        Lesson lesson = new Lesson();
        Mark mark = Mark.builder()
                .mark(85)
                .studentMark(null)
                .lessonMark(lesson)
                .build();

        Set<ConstraintViolation<Mark>> violations = validator.validate(mark);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Student cannot be null");
    }

    @Test
    void whenLessonMarkIsNull_thenOneViolation() {
        Student student = new Student();
        Mark mark = Mark.builder()
                .mark(85)
                .studentMark(student)
                .lessonMark(null)
                .build();

        Set<ConstraintViolation<Mark>> violations = validator.validate(mark);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Lesson cannot be null");
    }
}