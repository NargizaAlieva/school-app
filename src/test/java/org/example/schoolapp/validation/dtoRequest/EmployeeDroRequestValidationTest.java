package org.example.schoolapp.validation.dtoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeDroRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidEmployeeDroRequest() {
        EmployeeDroRequest request = EmployeeDroRequest.builder()
                .userId(1L)
                .position("Teacher")
                .salary(50000)
                .build();

        Set<ConstraintViolation<EmployeeDroRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNullUserId() {
        EmployeeDroRequest request = new EmployeeDroRequest();
        request.setPosition("Teacher");
        request.setSalary(50000);

        Set<ConstraintViolation<EmployeeDroRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("UserId cannot be null"));
    }
}