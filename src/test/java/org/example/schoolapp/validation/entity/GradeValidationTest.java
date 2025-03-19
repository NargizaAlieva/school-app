package org.example.schoolapp.validation.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GradeValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenGradeIsValid_thenNoViolations() {
        Employee classTeacher = new Employee();
        Grade grade = Grade.builder()
                .title("Grade 10")
                .classTeacher(classTeacher)
                .build();

        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenTitleIsNull_thenTwoViolations() {
        Employee classTeacher = new Employee();
        Grade grade = Grade.builder()
                .title(null)
                .classTeacher(classTeacher)
                .build();

        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);
        assertThat(violations).hasSize(2);

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertThat(messages).containsExactlyInAnyOrder(
                "Grade title cannot be null",
                "Grade title cannot be blank"
        );
    }

    @Test
    void whenTitleIsBlank_thenOneViolation() {
        Employee classTeacher = new Employee();
        Grade grade = Grade.builder()
                .title("")
                .classTeacher(classTeacher)
                .build();

        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Grade title cannot be blank");
    }

    @Test
    void whenClassTeacherIsNull_thenOneViolation() {
        Grade grade = Grade.builder()
                .title("Grade 10")
                .classTeacher(null)
                .build();

        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Class Teacher cannot be null");
    }
}