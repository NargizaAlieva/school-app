package org.example.schoolapp.validation.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenEmployeeIsValid_thenNoViolations() {
        User user = new User();
        Employee employee = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(user)
                .build();

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenSalaryIsNegative_thenOneViolation() {
        User user = new User();
        Employee employee = Employee.builder()
                .position("Teacher")
                .salary(-1000)
                .user(user)
                .build();

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Salary cannot be negative");
    }

    @Test
    void whenUserIsNull_thenOneViolation() {
        Employee employee = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(null)
                .build();

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("User cannot be null");
    }
}
