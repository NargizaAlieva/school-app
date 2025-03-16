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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Grade grade;
    private User activeUser;
    private User inactiveUser;
    private Student activeStudent;
    private Student inactiveStudent;
    private Employee teacher;

    @BeforeEach
    void setUp() {
        teacher = employeeRepository.save(new Employee());

        activeUser = new User();
        activeUser.setUsername("activeUser");
        activeUser.setFirstName("activeUser");
        activeUser.setLastName("activeUser");
        activeUser.setEmail("activeUser@email.com");
        activeUser.setPassword("activeUser");
        activeUser.setIsActive(true);
        activeUser = userRepository.save(activeUser);

        inactiveUser = new User();
        inactiveUser.setUsername("inactiveUser");
        inactiveUser.setFirstName("inactiveUser");
        inactiveUser.setLastName("inactiveUser");
        inactiveUser.setEmail("inactiveUser@email.com");
        inactiveUser.setPassword("inactiveUser");
        inactiveUser.setIsActive(false);
        inactiveUser = userRepository.save(inactiveUser);

        grade = new Grade();
        grade.setTitle("10B");
        grade.setClassTeacher(teacher);
        grade.setIsActive(true);
        grade = gradeRepository.save(grade);

        activeStudent = new Student();
        activeStudent.setUser(activeUser);
        activeStudent.setGrade(grade);
        activeStudent.setParentStatus(ParentStatus.MOTHER);
        activeStudent = studentRepository.save(activeStudent);

        inactiveStudent = new Student();
        inactiveStudent.setUser(inactiveUser);
        inactiveStudent.setGrade(grade);
        inactiveStudent.setParentStatus(ParentStatus.MOTHER);
        inactiveStudent = studentRepository.save(inactiveStudent);
    }

    @Test
    void testExistsByUserId() {
        boolean exists = studentRepository.existsByUserId(activeUser.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByUserId() {
        Optional<Student> student = studentRepository.findByUserId(activeUser.getId());
        assertThat(student).isPresent();
        assertThat(student.get().getUser().getId()).isEqualTo(activeUser.getId());
    }

    @Test
    void testFindAllActiveStudents() {
        List<Student> students = studentRepository.findAllActiveStudents();
        assertThat(students).contains(activeStudent);
        assertThat(students).doesNotContain(inactiveStudent);
    }

    @Test
    void testFindAllActiveStudentsByGrade() {
        List<Student> students = studentRepository.findAllActiveStudentsByGrade(grade.getId());
        assertThat(students).contains(activeStudent);
        assertThat(students).doesNotContain(inactiveStudent);
    }

    @Test
    void testFindAllActiveStudentsByGrades() {
        List<Student> students = studentRepository.findAllActiveStudentsByGrades(List.of(grade.getId()));
        assertThat(students).contains(activeStudent);
        assertThat(students).doesNotContain(inactiveStudent);
    }
}