package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.ParentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Employee teacher;
    private Student student;
    private Grade grade;

    @BeforeEach
    void setUp() {
        teacher = employeeRepository.save(new Employee());
        grade = new Grade();
        grade.setTitle("10B");
        grade.setClassTeacher(teacher);
        grade.setIsActive(true);
        grade = gradeRepository.save(grade);

        user = new User();
        user.setUsername("rosa");
        user.setFirstName("Rosa");
        user.setLastName("Ala");
        user.setEmail("rosa@example.com");
        user.setPassword("password");
        user.setIsActive(true);
        user = userRepository.save(user);

        student = new Student();
        student.setUser(user);
        student.setGrade(grade);
        student.setParentStatus(ParentStatus.MOTHER);
        student = studentRepository.save(student);
    }

    @Test
    void testFindAllByIsActiveTrue() {
        List<Grade> activeGrades = gradeRepository.findAllByIsActiveTrue();
        assertThat(activeGrades).isNotEmpty();
        assertThat(activeGrades.get(0).getIsActive()).isTrue();
    }

    @Test
    void testFindByClassTeacherId() {
        List<Grade> grades = gradeRepository.findByClassTeacherId(teacher.getId());
        assertThat(grades).isNotEmpty();
        assertThat(grades.get(0).getClassTeacher().getId()).isEqualTo(teacher.getId());
    }

    @Test
    void testGetGradeByStudentId() {
        Grade retrievedGrade = gradeRepository.getGradeByStudentId(student.getId());
        assertThat(retrievedGrade).isNotNull();
        assertThat(retrievedGrade.getId()).isEqualTo(grade.getId());
    }

    @Test
    void testFindAllByIdIn() {
        List<Grade> grades = gradeRepository.findAllByIdIn(Set.of(grade.getId()));
        assertThat(grades).isNotEmpty();
        assertThat(grades.get(0).getId()).isEqualTo(grade.getId());
    }
}
