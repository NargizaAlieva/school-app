package org.example.schoolapp.validation.entity;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ScheduleValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenScheduleIsValid_thenNoViolations() {
        Subject subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();

        Employee teacher = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .build();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .isActive(true)
                .build();

        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(subject)
                .teacherSchedule(teacher)
                .gradeSchedule(grade)
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenDayOfWeekIsNull_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(null)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Day of the week cannot be null");
    }

    @Test
    public void whenQuarterIsNull_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(null)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quarter cannot be null");
    }

    @Test
    public void whenQuarterIsLessThanOne_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(0)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quarter must be at least 1");
    }

    @Test
    public void whenQuarterIsGreaterThanFour_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(5)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quarter cannot exceed 4");
    }

    @Test
    public void whenDueTimeIsNull_thenTwoViolations() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime(null)
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(2); // Expecting 2 violations
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Due time cannot be null",
                "Due time cannot be empty"
        );
    }

    @Test
    public void whenDueTimeIsBlank_thenTwoViolations() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Due time cannot be empty",
                "Due time must be in HH:mm-HH:mm format"
        );
    }

    @Test
    public void whenSchoolYearIsNull_thenTwoViolations() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear(null)
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "School year cannot be null",
                "School year cannot be empty"
        );
    }

    @Test
    public void whenSchoolYearIsBlank_thenTwoViolations() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "School year cannot be empty",
                "School year must be in YYYY-YYYY format"
        );
    }

    @Test
    public void whenDueTimeIsInvalidFormat_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-25:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Due time must be in HH:mm-HH:mm format");
    }

    @Test
    public void whenSchoolYearIsInvalidFormat_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023/2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("School year must be in YYYY-YYYY format");
    }

    @Test
    public void whenSubjectIsNull_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(null)
                .teacherSchedule(new Employee())
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Subject cannot be null");
    }

    @Test
    public void whenTeacherIsNull_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(null)
                .gradeSchedule(new Grade())
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Teacher cannot be null");
    }

    @Test
    public void whenGradeIsNull_thenOneViolation() {
        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(new Subject())
                .teacherSchedule(new Employee())
                .gradeSchedule(null)
                .build();

        Set<ConstraintViolation<Schedule>> violations = validator.validate(schedule);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Grade cannot be null");
    }
}