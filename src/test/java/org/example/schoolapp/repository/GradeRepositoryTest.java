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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GradeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GradeRepository gradeRepository;

    private Grade grade;
    private Employee classTeacher;
    private Student student;

    @BeforeEach
    public void setUp() {
        User user1 = User.builder()
                .username("parentuser")
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user1);

        Parent parent = Parent.builder()
                .user(user1)
                .build();
        entityManager.persist(parent);

        User user2 = User.builder()
                .username("teacheruser")
                .firstName("Nana")
                .lastName("Mara")
                .email("nana.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user2);

        classTeacher = Employee.builder()
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

        User user3 = User.builder()
                .username("studentuser")
                .firstName("Marry")
                .lastName("Smith")
                .email("marry.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user3);

        student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.MOTHER)
                .user(user3)
                .parent(parent)
                .grade(grade)
                .build();
        entityManager.persist(student);

        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnGrade() {
        Grade foundGrade = gradeRepository.findById(grade.getId()).orElse(null);
        assertThat(foundGrade).isNotNull();
        assertThat(foundGrade.getTitle()).isEqualTo(grade.getTitle());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = gradeRepository.existsById(grade.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindAllByIsActiveTrue_thenReturnActiveGrades() {
        List<Grade> activeGrades = gradeRepository.findAllByIsActiveTrue();
        assertThat(activeGrades).isNotEmpty();
        assertThat(activeGrades.get(0).getIsActive()).isTrue();
    }

    @Test
    public void whenFindAllByIdIn_thenReturnGrades() {
        Set<Long> gradeIds = Set.of(grade.getId());
        List<Grade> foundGrades = gradeRepository.findAllByIdIn(gradeIds);

        assertThat(foundGrades).isNotEmpty();
        assertThat(foundGrades.get(0).getId()).isEqualTo(grade.getId());
    }

    @Test
    public void whenFindByClassTeacherId_thenReturnGrades() {
        List<Grade> foundGrades = gradeRepository.findByClassTeacherId(classTeacher.getId());
        assertThat(foundGrades).isNotEmpty();
        assertThat(foundGrades.get(0).getClassTeacher().getId()).isEqualTo(classTeacher.getId());
    }

    @Test
    public void whenGetGradeByStudentId_thenReturnGrade() {
        Grade foundGrade = gradeRepository.getGradeByStudentId(student.getId());
        assertThat(foundGrade).isNotNull();
        assertThat(foundGrade.getId()).isEqualTo(grade.getId());
    }
}