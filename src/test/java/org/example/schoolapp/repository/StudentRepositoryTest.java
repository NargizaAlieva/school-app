package org.example.schoolapp.repository;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.ParentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private Student student;
    private User user;
    private Parent parent;
    private Grade grade;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user);

        parent = Parent.builder()
                .user(user)
                .build();
        entityManager.persist(parent);

        User user2 = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("nana.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user2);

        Employee classTeacher = Employee.builder()
                .position("Teacher")
                .user(user2)
                .salary(50000)
                .build();
        entityManager.persist(classTeacher);

        grade = Grade.builder()
                .title("Grade 10A")
                .isActive(true)
                .classTeacher(classTeacher)
                .build();
        entityManager.persist(grade);

        user = User.builder()
                .firstName("Mark")
                .lastName("Smith")
                .email("mark.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user);

        student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.MOTHER)
                .user(user)
                .parent(parent)
                .grade(grade)
                .build();
        entityManager.persist(student);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnStudent() {
        Optional<Student> foundStudent = studentRepository.findById(student.getId());
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = studentRepository.existsById(student.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByUserId_thenReturnTrue() {
        boolean exists = studentRepository.existsByUserId(user.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByUserId_thenReturnStudent() {
        Optional<Student> foundStudent = studentRepository.findByUserId(user.getId());
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void whenFindAllActiveStudents_thenReturnActiveStudents() {
        List<Student> activeStudents = studentRepository.findAllActiveStudents();
        assertThat(activeStudents).isNotEmpty();
        assertThat(activeStudents.get(0).getUser().getIsActive()).isTrue();
    }

    @Test
    public void whenFindAllActiveStudentsByGrade_thenReturnActiveStudents() {
        List<Student> activeStudents = studentRepository.findAllActiveStudentsByGrade(grade.getId());
        assertThat(activeStudents).isNotEmpty();
        assertThat(activeStudents.get(0).getGrade().getId()).isEqualTo(grade.getId());
    }

    @Test
    public void whenFindAllActiveStudentsByGrades_thenReturnActiveStudents() {
        List<Long> gradeIds = List.of(grade.getId());
        List<Student> activeStudents = studentRepository.findAllActiveStudentsByGrades(gradeIds);

        assertThat(activeStudents).isNotEmpty();
        assertThat(activeStudents.get(0).getGrade().getId()).isEqualTo(grade.getId());
    }

    @Test
    public void whenFindParentStudentsByParentId_thenReturnStudents() {
        List<Student> parentStudents = studentRepository.findParentStudentsByParentId(parent.getId());
        assertThat(parentStudents).isNotEmpty();
        assertThat(parentStudents.get(0).getParent().getId()).isEqualTo(parent.getId());
    }
}