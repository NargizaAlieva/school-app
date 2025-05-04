package org.example.schoolapp.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.service.entity.*;
import org.example.schoolapp.service.role.PrincipleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrincipleServiceImpl implements PrincipleService {
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final EmployeeService employeeService;
    private final SubjectService subjectService;

    private Employee getPrinciple() {
        return employeeService.findByUserId(userService.getCurrentUser().getId());
    }

    @Override
    public ScheduleDto getScheduleById(Long id) {
        return scheduleService.getScheduleById(id);
    }

    @Override
    public ScheduleDto approveSchedule(Long id) {
        return scheduleService.approveSchedule(id);
    }
    @Override
    public ScheduleDto disapproveSchedule(Long id) {
        return scheduleService.disapproveSchedule(id);
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleService.deleteSchedule(id);
    }

    @Override
    public ScheduleDto restoreSchedule(Long id) {
        return scheduleService.restoreSchedule(id);
    }


    @Override
    public List<ScheduleDto> getAllSchedule() {
        return scheduleService.getAllSchedule();
    }

    @Override
    public List<ScheduleDto> getAllActiveSchedule() {
        return scheduleService.getAllActiveSchedule();
    }

    @Override
    public List<ScheduleDto> getAllScheduleByYear(String year) {
        return scheduleService.getAllScheduleByYear(year);
    }

    @Override
    public List<ScheduleDto> getAllUnapprovedSchedule() {
        return scheduleService.getAllUnApprovedSchedule();
    }

    @Override
    public ScheduleDto createSchedule(ScheduleDtoRequest scheduleDtoRequest) {
        return scheduleService.createSchedule(scheduleDtoRequest);
    }

    @Override
    public ScheduleDto updateSchedule(ScheduleDtoRequest scheduleDtoRequest) {
        return scheduleService.updateSchedule(scheduleDtoRequest);
    }


    @Override
    public EmployeeDto hireEmployee(EmployeeDroRequest employeeDtoR) {
        return employeeService.createEmployee(employeeDtoR);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDroRequest employeeDtoR) {
        return employeeService.updateEmployee(employeeDtoR);
    }

    @Override
    public List<EmployeeDto> getAllEmployee() {
        return employeeService.getAllEmployee();
    }

    @Override
    public List<EmployeeDto> getAllActiveEmployee() {
        return employeeService.getAllActiveEmployee();
    }

    @Override
    public void fireEmployee(Long id) {
        employeeService.deleteEmployee(id);
    }

    @Override
    public SubjectDto addSubject(SubjectDtoRequest subjectDtoRequest) {
        return subjectService.createSubject(subjectDtoRequest);
    }

    @Override
    public SubjectDto updateSubject(SubjectDtoRequest subjectDtoRequest) {
        return subjectService.updateSubject(subjectDtoRequest);
    }

    @Override
    public void deleteSubject(Long id) {
        subjectService.deleteSubject(id);
    }

    @Override
    public SubjectDto restoreSubject(Long id) {
        return subjectService.restoreSubject(id);
    }

    @Override
    public List<SubjectDto> getAllSubject() {
        return subjectService.getAllSubject();
    }

    @Override
    public List<SubjectDto> getAllActiveSubject() {
        return subjectService.getAllActiveSubject();
    }
}
