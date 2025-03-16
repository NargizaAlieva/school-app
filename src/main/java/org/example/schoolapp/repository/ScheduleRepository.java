package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    boolean existsById(Long id);

    List<Schedule> findAllByIsActiveTrue();

    List<Schedule> findByIsApproveFalse();
    List<Schedule> findBySchoolYear(String schoolYear);
    List<Schedule> findByGradeScheduleId(Long gradeId);
    List<Schedule> findByTeacherScheduleId(Long teacherId);
}
