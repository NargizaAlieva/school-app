package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Schedule;
import org.example.schoolapp.enums.DaysOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Schedule activeSchedule;
    private Schedule inactiveSchedule;
    private Schedule approvedSchedule;
    private Schedule unapprovedSchedule;

    @BeforeEach
    void setUp() {
        activeSchedule = new Schedule();
        activeSchedule.setIsActive(true);
        activeSchedule.setIsApprove(true);
        activeSchedule.setQuarter(1);
        activeSchedule.setDueTime("10:30");
        activeSchedule.setSchoolYear("2024");
        activeSchedule.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleRepository.save(activeSchedule);

        inactiveSchedule = new Schedule();
        inactiveSchedule.setIsActive(false);
        inactiveSchedule.setQuarter(1);
        inactiveSchedule.setIsApprove(false);
        inactiveSchedule.setSchoolYear("2023");
        inactiveSchedule.setDueTime("10:30");
        inactiveSchedule.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleRepository.save(inactiveSchedule);

        approvedSchedule = new Schedule();
        approvedSchedule.setIsApprove(true);
        approvedSchedule.setIsActive(true);
        approvedSchedule.setQuarter(1);
        approvedSchedule.setSchoolYear("2020");
        approvedSchedule.setDueTime("10:30");
        approvedSchedule.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleRepository.save(approvedSchedule);

        unapprovedSchedule = new Schedule();
        unapprovedSchedule.setIsApprove(false);
        unapprovedSchedule.setIsActive(true);
        unapprovedSchedule.setSchoolYear("2020");
        unapprovedSchedule.setQuarter(1);
        unapprovedSchedule.setDueTime("10:30");
        unapprovedSchedule.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleRepository.save(unapprovedSchedule);
    }

    @Test
    void testFindAllByIsActiveTrue() {
        List<Schedule> activeSchedules = scheduleRepository.findAllByIsActiveTrue();
        assertThat(activeSchedules).contains(activeSchedule);
        assertThat(activeSchedules).doesNotContain(inactiveSchedule);
    }

    @Test
    void testFindByIsApproveFalse() {
        List<Schedule> unapprovedSchedules = scheduleRepository.findByIsApproveFalse();
        assertThat(unapprovedSchedules).contains(unapprovedSchedule);
        assertThat(unapprovedSchedules).doesNotContain(approvedSchedule);
    }

    @Test
    void testFindBySchoolYear() {
        List<Schedule> schedules = scheduleRepository.findBySchoolYear("2024");
        assertThat(schedules).contains(activeSchedule);
        assertThat(schedules).doesNotContain(inactiveSchedule);
    }
}