package org.example.schoolapp.repository;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LessonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LessonRepository lessonRepository;

    private Lesson lesson;
    private Subject subject;
    private Employee teacher;
    private Grade grade;

    @BeforeEach
    public void setUp() {
        subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);

        User user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .phone("+1234567890")
                .email("john.doe@example.com")
                .password("Password123!")
                .isActive(true)
                .build();

        teacher = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(user1)
                .build();
        entityManager.persist(teacher);

        grade = Grade.builder()
                .title("Grade 10A")
                .classTeacher(teacher)
                .isActive(true)
                .build();
        entityManager.persist(grade);

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
                .creationDate(LocalDateTime.now())
                .schedule(schedule)
                .build();
        entityManager.persist(lesson);

        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnLesson() {
        Lesson foundLesson = lessonRepository.findById(lesson.getId()).orElse(null);
        assertThat(foundLesson).isNotNull();
        assertThat(foundLesson.getTopic()).isEqualTo(lesson.getTopic());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = lessonRepository.existsById(lesson.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenGetAllLessonsByTeacherId_thenReturnLessons() {
        List<Lesson> foundLessons = lessonRepository.getAllLessonsByTeacherId(teacher.getId());
        assertThat(foundLessons).isNotEmpty();
        assertThat(foundLessons.get(0).getSchedule().getTeacherSchedule().getId()).isEqualTo(teacher.getId());
    }

    @Test
    public void whenGetAllLessonsByGradeId_thenReturnLessons() {
        List<Lesson> foundLessons = lessonRepository.getAllLessonsByGradeId(grade.getId());
        assertThat(foundLessons).isNotEmpty();
        assertThat(foundLessons.get(0).getSchedule().getGradeSchedule().getId()).isEqualTo(grade.getId());
    }

    @Test
    public void whenGetAllLessonsBySubjectId_thenReturnLessons() {
        List<Lesson> foundLessons = lessonRepository.getAllLessonsBySubjectId(subject.getId());
        assertThat(foundLessons).isNotEmpty();
        assertThat(foundLessons.get(0).getSchedule().getSubjectSchedule().getId()).isEqualTo(subject.getId());
    }

    @Test
    public void whenGetAllLessonsByYear_thenReturnLessons() {
        List<Lesson> foundLessons = lessonRepository.getAllLessonsByYear("2023-2024");
        assertThat(foundLessons).isNotEmpty();
        assertThat(foundLessons.get(0).getSchedule().getSchoolYear()).isEqualTo("2023-2024");
    }

    @Test
    public void whenGetAllLessonsByQuarter_thenReturnLessons() {
        List<Lesson> foundLessons = lessonRepository.getAllLessonsByQuarter(1);
        assertThat(foundLessons).isNotEmpty();
        assertThat(foundLessons.get(0).getSchedule().getQuarter()).isEqualTo(1);
    }

    @Test
    public void whenGetAllLessonBySubjectGrade_thenReturnLessons() {
        List<Lesson> foundLessons = lessonRepository.getAllLessonBySubjectGrade(subject.getId(), grade.getId());
        assertThat(foundLessons).isNotEmpty();
        assertThat(foundLessons.get(0).getSchedule().getSubjectSchedule().getId()).isEqualTo(subject.getId());
        assertThat(foundLessons.get(0).getSchedule().getGradeSchedule().getId()).isEqualTo(grade.getId());
    }
}