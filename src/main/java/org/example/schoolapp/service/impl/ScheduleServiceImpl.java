package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.entity.Schedule;
import org.example.schoolapp.repository.ScheduleRepository;
import org.example.schoolapp.service.EmployeeService;
import org.example.schoolapp.service.ScheduleService;
import org.example.schoolapp.service.StudentService;
import org.example.schoolapp.util.exception.*;
import org.example.schoolapp.util.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final StudentService studentService;

    public List<Schedule> getAllScheduleEntity() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule getScheduleByIdEntity(Long id) {
        if (id == null) throw new IncorrectRequestException("id");
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule with id: '" + id + "' not found"));
    }

    @Override
    public ScheduleDto getScheduleById(Long id) {
        return scheduleMapper.toScheduleDto(getScheduleByIdEntity(id));
    }

    @Override
    public List<ScheduleDto> getAllSchedule() {
        return scheduleMapper.toScheduleDtoList(getAllScheduleEntity());
    }

    @Override
    public List<ScheduleDto> getAllActiveSchedule() {
        List<Schedule> activeSchedules = scheduleRepository.findAllByIsActiveTrue();
        return scheduleMapper.toScheduleDtoList(activeSchedules);
    }

    @Override
    public List<ScheduleDto> getAllScheduleByTeacher(Long teacherId) {
        List<Schedule> schedules = scheduleRepository.findByTeacherScheduleId(teacherId);
        return scheduleMapper.toScheduleDtoList(schedules);
    }

    @Override
    public List<ScheduleDto> getAllScheduleByGrade(Long gradeId) {
        List<Schedule> schedules = scheduleRepository.findByGradeScheduleId(gradeId);
        return scheduleMapper.toScheduleDtoList(schedules);
    }

    @Override
    public List<ScheduleDto> getAllScheduleByYear(String year) {
        List<Schedule> schedules = scheduleRepository.findBySchoolYear(year);
        return scheduleMapper.toScheduleDtoList(schedules);
    }

    @Override
    public List<ScheduleDto> getAllScheduleByStudent(Long studentId) {
        List<Schedule> scheduleDtoList = new ArrayList<>();
        for (Schedule s : getAllScheduleEntity())
            if (s.getGradeSchedule().getId().equals(studentService.getStudentByIdEntity(studentId).getGrade().getId()))
                scheduleDtoList.add(s);
        return scheduleMapper.toScheduleDtoList(scheduleDtoList);
    }

    @Override
    public List<ScheduleDto> getAllUnApprovedSchedule() {
        List<Schedule> unapprovedSchedules = scheduleRepository.findByIsApproveFalse();
        return scheduleMapper.toScheduleDtoList(unapprovedSchedules);
    }

    @Override
    public ScheduleDto createSchedule(ScheduleDtoRequest scheduleDtoRequest) {
        return scheduleMapper.toScheduleDto(save(scheduleMapper.toScheduleEntity(scheduleDtoRequest)));
    }

    @Override
    public ScheduleDto updateSchedule(ScheduleDtoRequest scheduleDtoRequest) {
        Schedule request = scheduleMapper.toScheduleEntity(scheduleDtoRequest);
        Schedule schedule = getScheduleByIdEntity(scheduleDtoRequest.getId());

        if (request.getIsActive() == null)
            request.setIsActive(schedule.getIsActive());

        if (request.getIsApprove() == null)
            request.setIsApprove(schedule.getIsApprove());

        schedule = schedule.toBuilder()
                .dayOfWeek(request.getDayOfWeek())
                .dueTime(request.getDueTime())
                .quarter(request.getQuarter())
                .schoolYear(request.getSchoolYear())
                .subjectSchedule(request.getSubjectSchedule())
                .gradeSchedule(request.getGradeSchedule())
                .teacherSchedule(request.getTeacherSchedule())
                .isApprove(request.getIsApprove())
                .isActive(request.getIsActive())
                .build();

        return scheduleMapper.toScheduleDto(save(schedule));
    }

    @Override
    public void deleteSchedule(Long id) {
        Schedule schedule = getScheduleByIdEntity(id);

        if (!schedule.getIsActive())
            throw new AlreadyDisabledException("Schedule", id);

        schedule.setIsActive(false);
        scheduleMapper.toScheduleDto(save(schedule));
    }

    @Override
    public ScheduleDto restoreSchedule(Long id) {
        Schedule schedule = getScheduleByIdEntity(id);

        if (schedule.getIsActive())
            throw new AlreadyEnabledException("Schedule", id);

        schedule.setIsActive(true);
        return scheduleMapper.toScheduleDto(save(schedule));
    }

    @Override
    public ScheduleDto approveSchedule(Long id) {
        Schedule schedule = getScheduleByIdEntity(id);
        schedule.setIsApprove(true);
        return scheduleMapper.toScheduleDto(save(schedule));
    }

    @Override
    public ScheduleDto disapproveSchedule(Long id) {
        Schedule schedule = getScheduleByIdEntity(id);
        schedule.setIsApprove(false);
        return scheduleMapper.toScheduleDto(save(schedule));
    }
}
