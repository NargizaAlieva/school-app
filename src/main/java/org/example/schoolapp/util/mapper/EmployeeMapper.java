package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.SubjectService;
import org.example.schoolapp.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final UserMapper userMapper;
    private final UserService userService;
    private final SubjectService subjectService;

    public EmployeeDto toEmployeeDto(Employee employee) {
        return new EmployeeDto().toBuilder()
                .id(employee.getId())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .userDto(userMapper.toUserDto(employee.getUser()))
                .subjectSet(subjectToString(employee))
                .build();
    }

    public List<EmployeeDto> toEmployeeDtoList(List<Employee> employees) {
        return employees.stream().map(this::toEmployeeDto).collect(Collectors.toList());
    }

    public Set<EmployeeDto> toEmployeeDtoSet(Set<Employee> employees) {
        return employees.stream().map(this::toEmployeeDto).collect(Collectors.toSet());
    }

    public Employee toEmployeeEntity(EmployeeDroRequest employeeDroRequest) {
        User user = userService.getEntityById(employeeDroRequest.getUserId());

        return new Employee().toBuilder()
                .id(employeeDroRequest.getId())
                .position(employeeDroRequest.getPosition())
                .salary(employeeDroRequest.getSalary())
                .user(user)
                .subjectSet(idToSubject(employeeDroRequest))
                .build();
    }

    private Set<String> subjectToString (Employee employee) {
        Set<Subject> subjectDtoSet = employee.getSubjectSet();
        Set<String> subjectStrings = new HashSet<>();

        for (Subject s : subjectDtoSet)
            subjectStrings.add(s.getTitle());

        return subjectStrings;
    }

    private Set<Subject> idToSubject(EmployeeDroRequest employeeDroRequest) {
        Set<Subject> subjectSet = new HashSet<>();

        for (Long s : employeeDroRequest.getSubjectSet())
            subjectSet.add(subjectService.findByIdEntity(s));

        return subjectSet;
    }
}
