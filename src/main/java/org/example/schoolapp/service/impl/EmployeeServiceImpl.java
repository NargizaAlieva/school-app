package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.repository.EmployeeRepository;
import org.example.schoolapp.service.EmployeeService;
import org.example.schoolapp.service.UserService;
import org.example.schoolapp.util.exception.AlreadyExistException;
import org.example.schoolapp.util.exception.IncorrectRequestException;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    public Boolean isExistByUserId(Long id) {
        return employeeRepository.existsByUserId(id);
    }

    @Override
    public Employee save (Employee employee){
        return employeeRepository.save(employee);
    }

    @Override
    public Employee findByIdEntity(Long id) {
        if (id == null)
            throw new IncorrectRequestException("Id cannot be null");
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Employee with id: '" + id + "' not found"));
    }

    @Override
    public EmployeeDto getDtoById(Long id) {
        return employeeMapper.toEmployeeDto(findByIdEntity(id));
    }

    @Override
    public List<EmployeeDto> getAllEmployee() {
        return employeeMapper.toEmployeeDtoList(employeeRepository.findAll());
    }

    @Override
    public List<EmployeeDto> getAllActiveEmployee() {
        List<Employee> activeEmployees = employeeRepository.findAllActiveEmployees();
        return employeeMapper.toEmployeeDtoList(activeEmployees);
    }

    @Override
    public List<EmployeeDto> getBySubjectId(Long subjectId) {
        return employeeMapper.toEmployeeDtoList(employeeRepository.findBySubjectId(subjectId));
    }

    @Override
    public EmployeeDto getHomeTeacherByGradeId(Long gradeId) {
        return employeeMapper.toEmployeeDto(employeeRepository.getHomeTeacherByGradeId(gradeId));
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDroRequest request) {
        if (isExistByUserId(request.getUserId()))
            throw new AlreadyExistException("Employee", "userId", String.valueOf(request.getUserId()));
        Employee employee = employeeMapper.toEmployeeEntity(request);
        employeeMapper.toEmployeeDto(employee);

        return employeeMapper.toEmployeeDto(save(employeeMapper.toEmployeeEntity(request)));
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDroRequest employeeDroRequest) {
        if (userService.getById(employeeDroRequest.getUserId()) == null)
            throw new IncorrectRequestException("User");

        Employee request = employeeMapper.toEmployeeEntity(employeeDroRequest);
        Employee employee = findByIdEntity(employeeDroRequest.getId());

        if(!employee.getUser().getId().equals(request.getUser().getId()))
            if (isExistByUserId(employeeDroRequest.getUserId()))
                throw new AlreadyExistException("Employee", "userId", String.valueOf(employeeDroRequest.getUserId()));

        if (request.getSubjectSet() == null)
            request.setSubjectSet(employee.getSubjectSet());

        if (request.getPosition() == null)
            request.setPosition(employee.getPosition());

        if (request.getSalary() == null)
            request.setSalary(employee.getSalary());

        employee = employee.toBuilder()
                .position(request.getPosition())
                .salary(request.getSalary())
                .user(request.getUser())
                .subjectSet(request.getSubjectSet())
                .build();

        return employeeMapper.toEmployeeDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = findByIdEntity(id);
        userService.deleteUser(employee.getUser().getId());
    }

    @Override
    public EmployeeDto restoreEmployee(Long id) {
        Employee employee = findByIdEntity(id);
        userService.restoreUser(employee.getUser().getId());
        return employeeMapper.toEmployeeDto(employee);
    }
}
