package org.example.schoolapp.repository;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MarkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarkRepository markRepository;

    private Mark mark;
    private Student student;
    private Lesson lesson;
    private Subject subject;

    @BeforeEach
    public void setUp() {
        User user1 = User.builder()
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

        subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);

        User user2 = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("nana.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user2);

        Employee teacher = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(user2)
                .build();
        entityManager.persist(teacher);

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .classTeacher(teacher)
                .isActive(true)
                .build();
        entityManager.persist(grade);

        User user3 = User.builder()
                .firstName("Mark")
                .lastName("Smith")
                .email("mark.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user3);

        student = Student.builder()
                .parentStatus(ParentStatus.FATHER)
                .user(user3)
                .grade(grade)
                .parent(parent)
                .build();
        entityManager.persist(student);

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
        entityManager.persist(schedule);

        lesson = Lesson.builder()
                .topic("Algebra Basics")
                .homework("Complete exercises 1-10")
                .schedule(schedule)
                .build();
        entityManager.persist(lesson);

        mark = Mark.builder()
                .mark(85)
                .studentMark(student)
                .lessonMark(lesson)
                .build();
        entityManager.persist(mark);

        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnMark() {
        Mark foundMark = markRepository.findById(mark.getId()).orElse(null);
        assertThat(foundMark).isNotNull();
        assertThat(foundMark.getMark()).isEqualTo(mark.getMark());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = markRepository.existsById(mark.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByStudentMarkId_thenReturnMarks() {
        List<Mark> foundMarks = markRepository.findByStudentMarkId(student.getId());
        assertThat(foundMarks).isNotEmpty();
        assertThat(foundMarks.get(0).getStudentMark().getId()).isEqualTo(student.getId());
    }

    @Test
    public void whenFindMarksByStudentAndSchedule_thenReturnMarks() {
        List<Mark> foundMarks = markRepository.findMarksByStudentAndSchedule(
                student.getId(),
                subject.getId(),
                "2023-2024",
                1
        );

        assertThat(foundMarks).isNotEmpty();
        assertThat(foundMarks.get(0).getStudentMark().getId()).isEqualTo(student.getId());
        assertThat(foundMarks.get(0).getLessonMark().getSchedule().getSubjectSchedule().getId()).isEqualTo(subject.getId());
    }

    @Test
    public void whenFindMarksByStudentAndScheduleWithoutSubject_thenReturnMarks() {
        List<Mark> foundMarks = markRepository.findMarksByStudentAndSchedule(
                student.getId(),
                "2023-2024",
                1
        );

        assertThat(foundMarks).isNotEmpty();
        assertThat(foundMarks.get(0).getStudentMark().getId()).isEqualTo(student.getId());
    }

    @Test
    public void whenFindMarksByStudentAndYear_thenReturnMarks() {
        List<Mark> foundMarks = markRepository.findMarksByStudentAndSchedule(
                student.getId(),
                "2023-2024"
        );

        assertThat(foundMarks).isNotEmpty();
        assertThat(foundMarks.get(0).getStudentMark().getId()).isEqualTo(student.getId());
    }

    @Test
    public void whenFindAverageMarkByStudentId_thenReturnAverage() {
        Mark anotherMark = Mark.builder()
                .mark(90)
                .studentMark(student)
                .lessonMark(lesson)
                .build();
        entityManager.persist(anotherMark);
        entityManager.flush();

        Double averageMark = markRepository.findAverageMarkByStudentId(student.getId());
        assertThat(averageMark).isEqualTo(87.5); // (85 + 90) / 2 = 87.5
    }

    @Test
    public void whenFindAverageMarkByStudentIdAndSubjectId_thenReturnAverage() {
        Mark anotherMark = Mark.builder()
                .mark(90)
                .studentMark(student)
                .lessonMark(lesson)
                .build();
        entityManager.persist(anotherMark);
        entityManager.flush();

        Double averageMark = markRepository.findAverageMarkByStudentIdAndSubjectId(student.getId(), subject.getId());
        assertThat(averageMark).isEqualTo(87.5); // (85 + 90) / 2 = 87.5
    }
}