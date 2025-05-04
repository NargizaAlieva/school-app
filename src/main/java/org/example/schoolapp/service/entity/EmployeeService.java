package org.example.schoolapp.service.entity;

import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee save(Employee employee);
    Employee findByIdEntity(Long id);
    Employee findByUserId(Long id);
    EmployeeDto getDtoById(Long id);
    List<Long> getHomeClassesId();

    List<EmployeeDto> getAllEmployee();
    List<EmployeeDto> getAllActiveEmployee();

    List<EmployeeDto> getBySubjectId(Long subjectId);

    EmployeeDto getHomeTeacherByGradeId(Long gradeId);

    EmployeeDto createEmployee(EmployeeDroRequest employeeDroRequest);
    EmployeeDto updateEmployee(EmployeeDroRequest employeeDroRequest);

    void deleteEmployee(Long id);
    EmployeeDto restoreEmployee(Long id);
}
