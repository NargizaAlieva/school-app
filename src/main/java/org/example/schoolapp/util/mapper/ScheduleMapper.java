package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.entity.Schedule;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.service.EmployeeService;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.service.SubjectService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {
    private final SubjectService subjectService;
    private final EmployeeService employeeService;
    private final GradeService gradeService;

    public ScheduleDto toScheduleDto(Schedule schedule) {
        return new ScheduleDto().toBuilder()
                .id(schedule.getId())
                .dayOfWeek(schedule.getDayOfWeek())
                .dueTime(schedule.getDueTime())
                .quarter(schedule.getQuarter())
                .schoolYear(schedule.getSchoolYear())
                .subjectId(schedule.getSubjectSchedule().getId())
                .subjectTitle(schedule.getSubjectSchedule().getTitle())
                .gradeId(schedule.getGradeSchedule().getId())
                .gradeName(schedule.getGradeSchedule().getTitle())
                .teacherName(teacherToString(schedule))
                .isApprove(schedule.getIsApprove())
                .isActive(schedule.getIsActive())
                .build();
    }

    public List<ScheduleDto> toScheduleDtoList(List<Schedule> schedules) {
        return schedules.stream().map(this::toScheduleDto).collect(Collectors.toList());
    }

    public Schedule toScheduleEntity(ScheduleDtoRequest scheduleDtoRequest) {
        return new Schedule().toBuilder()
                .id(scheduleDtoRequest.getId())
                .quarter(scheduleDtoRequest.getQuarter())
                .schoolYear(scheduleDtoRequest.getSchoolYear())
                .isApprove(scheduleDtoRequest.getIsApprove())
                .isActive(scheduleDtoRequest.getIsActive())
                .subjectSchedule(subjectService.findByIdEntity(scheduleDtoRequest.getSubjectId()))
                .gradeSchedule(gradeService.getByIdEntity(scheduleDtoRequest.getGradeId()))
                .dayOfWeek(stringToDayOfWeek(scheduleDtoRequest))
                .dueTime(scheduleDtoRequest.getDueTime())
                .teacherSchedule(employeeService.findByIdEntity(scheduleDtoRequest.getTeacherId()))
                .build();
    }

    private String teacherToString(Schedule schedule) {
        User teacher = schedule.getTeacherSchedule().getUser();
        return teacher.getFirstName() + " " + teacher.getLastName();
    }

    private DaysOfWeek stringToDayOfWeek(ScheduleDtoRequest scheduleDtoRequest) {
        for (DaysOfWeek d : DaysOfWeek.values()) {
            if (scheduleDtoRequest.getDayOfWeek().toUpperCase().equals(d.name()))
                return d;
        }

        return null;
    }
}
