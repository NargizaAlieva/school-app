package org.example.schoolapp.validation.entity;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.ParentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenStudentIsValid_thenNoViolations() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        Parent parent = Parent.builder()
                .user(user)
                .build();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .isActive(true)
                .build();

        Student student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(user)
                .parent(parent)
                .grade(grade)
                .build();

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenParentStatusIsNull_thenOneViolation() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        Parent parent = Parent.builder()
                .user(user)
                .build();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .isActive(true)
                .build();

        Student student = Student.builder()
                .birthday(new Date())
                .parentStatus(null)
                .user(user)
                .parent(parent)
                .grade(grade)
                .build();

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Parent status cannot be null");
    }

    @Test
    public void whenUserIsNull_thenOneViolation() {
        Parent parent = Parent.builder()
                .user(User.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane.doe@example.com")
                        .password("password123")
                        .build())
                .build();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .isActive(true)
                .build();

        Student student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(null)
                .parent(parent)
                .grade(grade)
                .build();

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("User cannot be null");
    }

    @Test
    public void whenParentIsNull_thenOneViolation() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .isActive(true)
                .build();

        Student student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(user)
                .parent(null)
                .grade(grade)
                .build();

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Parent cannot be null");
    }

    @Test
    public void whenGradeIsNull_thenOneViolation() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        Parent parent = Parent.builder()
                .user(user)
                .build();

        Student student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(user)
                .parent(parent)
                .grade(null)
                .build();

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Grade cannot be null");
    }
}