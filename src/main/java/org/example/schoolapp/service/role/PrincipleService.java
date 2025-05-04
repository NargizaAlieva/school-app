package org.example.schoolapp.service.role;

import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.dto.request.*;

import java.util.List;

public interface PrincipleService {
    ScheduleDto getScheduleById(Long id);
    ScheduleDto approveSchedule(Long id);
    ScheduleDto disapproveSchedule(Long id);
    void deleteSchedule(Long id);
    ScheduleDto restoreSchedule(Long id);

    List<ScheduleDto> getAllSchedule();
    List<ScheduleDto> getAllActiveSchedule();
    List<ScheduleDto> getAllScheduleByYear(String year);
    List<ScheduleDto> getAllUnapprovedSchedule();
    ScheduleDto createSchedule(ScheduleDtoRequest scheduleDtoRequest);
    ScheduleDto updateSchedule(ScheduleDtoRequest scheduleDtoRequest);

    EmployeeDto hireEmployee(EmployeeDroRequest employeeDtoR);
    EmployeeDto updateEmployee(EmployeeDroRequest employeeDtoR);
    List<EmployeeDto> getAllEmployee();
    List<EmployeeDto> getAllActiveEmployee();
    void fireEmployee(Long id);

    SubjectDto addSubject(SubjectDtoRequest subjectDtoRequest);
    SubjectDto updateSubject(SubjectDtoRequest subjectDtoRequest);
    void deleteSubject(Long id);
    SubjectDto restoreSubject(Long id);

    List<SubjectDto> getAllSubject();
    List<SubjectDto> getAllActiveSubject();
}
