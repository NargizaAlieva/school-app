package org.example.schoolapp.repository;

import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ScheduleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Schedule schedule;
    private Employee teacher;
    private Grade grade;

    @BeforeEach
    public void setUp() {
        Subject subject = Subject.builder()
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

        schedule = Schedule.builder()
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
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnSchedule() {
        Schedule foundSchedule = scheduleRepository.findById(schedule.getId()).orElse(null);
        assertThat(foundSchedule).isNotNull();
        assertThat(foundSchedule.getDayOfWeek()).isEqualTo(schedule.getDayOfWeek());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = scheduleRepository.existsById(schedule.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindAllByIsActiveTrue_thenReturnActiveSchedules() {
        List<Schedule> activeSchedules = scheduleRepository.findAllByIsActiveTrue();
        assertThat(activeSchedules).isNotEmpty();
        assertThat(activeSchedules.get(0).getIsActive()).isTrue();
    }

    @Test
    public void whenFindByIsApproveFalse_thenReturnUnapprovedSchedules() {
        List<Schedule> unapprovedSchedules = scheduleRepository.findByIsApproveFalse();
        assertThat(unapprovedSchedules).isNotEmpty();
        assertThat(unapprovedSchedules.get(0).getIsApprove()).isFalse();
    }

    @Test
    public void whenFindBySchoolYear_thenReturnSchedules() {
        List<Schedule> foundSchedules = scheduleRepository.findBySchoolYear("2023-2024");
        assertThat(foundSchedules).isNotEmpty();
        assertThat(foundSchedules.get(0).getSchoolYear()).isEqualTo("2023-2024");
    }

    @Test
    public void whenFindByGradeScheduleId_thenReturnSchedules() {
        List<Schedule> foundSchedules = scheduleRepository.findByGradeScheduleId(grade.getId());
        assertThat(foundSchedules).isNotEmpty();
        assertThat(foundSchedules.get(0).getGradeSchedule().getId()).isEqualTo(grade.getId());
    }

    @Test
    public void whenFindByTeacherScheduleId_thenReturnSchedules() {
        List<Schedule> foundSchedules = scheduleRepository.findByTeacherScheduleId(teacher.getId());
        assertThat(foundSchedules).isNotEmpty();
        assertThat(foundSchedules.get(0).getTeacherSchedule().getId()).isEqualTo(teacher.getId());
    }
}