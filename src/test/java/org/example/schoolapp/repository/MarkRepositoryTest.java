package org.example.schoolapp.repository;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
class MarkRepositoryTest {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private Student student;
    private Mark mark;
    private Schedule schedule;
    private Subject subject;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("securepassword");
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setIsActive(true);

        student = new Student();
        student.setUser(user);
        student.setParentStatus(ParentStatus.MOTHER);
        student = studentRepository.save(student);

        subject = new Subject();
        subject.setTitle("Mathematics");
        subject.setIsActive(true);
        subject = subjectRepository.save(subject);

        schedule = new Schedule();
        schedule.setSchoolYear("2024");
        schedule.setQuarter(1);
        schedule.setDayOfWeek(DaysOfWeek.MONDAY);
        schedule.setDueTime("10:30");
        schedule.setSubjectSchedule(subject);
        schedule.setIsActive(true);
        schedule.setIsApprove(true);
        schedule = scheduleRepository.save(schedule);

        lesson = new Lesson();
        lesson.setTopic("Mathematics");
        lesson.setSchedule(schedule);
        lesson = lessonRepository.save(lesson);

        mark = new Mark();
        mark.setMark(100);
        mark.setStudentMark(student);
        mark.setLessonMark(lesson); // Assume lesson exists
        mark = markRepository.save(mark);
    }

    @Test
    void testExistsById() {
        boolean exists = markRepository.existsById(mark.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByStudentMarkId() {
        List<Mark> marks = markRepository.findByStudentMarkId(student.getId());
        assertThat(marks).isNotEmpty();
    }

    @Test
    void testFindMarksByStudentAndSchedule() {
        List<Mark> marks = markRepository.findMarksByStudentAndSchedule(student.getId(), subject.getId(), "2024", 1);
        assertThat(marks).isNotEmpty();
    }

    @Test
    void testFindMarksByStudentAndYear() {
        List<Mark> marks = markRepository.findMarksByStudentAndSchedule(student.getId(), "2024");
        assertThat(marks).isNotEmpty();
    }

    @Test
    void testFindMarksByStudentAndYearAndQuarter() {
        List<Mark> marks = markRepository.findMarksByStudentAndSchedule(student.getId(), "2024", 1);
        assertThat(marks).isNotEmpty();
    }
}