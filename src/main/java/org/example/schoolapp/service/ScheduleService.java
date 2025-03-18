package org.example.schoolapp.service;

import org.example.schoolapp.entity.Schedule;
import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;

import java.util.List;

public interface ScheduleService {
    Schedule save(Schedule schedule);

    Schedule getScheduleByIdEntity(Long id);
    ScheduleDto getScheduleById(Long id);

    List<ScheduleDto> getAllSchedule();
    List<ScheduleDto> getAllUnApprovedSchedule();

    List<ScheduleDto> getAllActiveSchedule();

    List<ScheduleDto> getAllScheduleByTeacher(Long teacherId);
    List<ScheduleDto> getAllScheduleByGrade(Long gradeId);
    List<ScheduleDto> getAllScheduleByYear(String year);

    List<ScheduleDto> getAllScheduleByStudent(Long studentId);

    ScheduleDto createSchedule(ScheduleDtoRequest scheduleDtoRequest);
    ScheduleDto updateSchedule(ScheduleDtoRequest scheduleDtoRequest);

    void deleteSchedule(Long id);
    ScheduleDto restoreSchedule(Long id);

    ScheduleDto approveSchedule(Long id);
    ScheduleDto disapproveSchedule(Long id);
}
