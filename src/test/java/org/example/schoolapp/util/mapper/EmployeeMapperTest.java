package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.SubjectService;
import org.example.schoolapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private EmployeeMapper employeeMapper;

    private Employee employee;
    private User user;
    private Subject subject;
    private EmployeeDroRequest employeeDroRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        subject = new Subject();
        subject.setId(100L);
        subject.setTitle("Mathematics");

        employee = new Employee();
        employee.setId(10L);
        employee.setPosition("Teacher");
        employee.setSalary(5000);
        employee.setUser(user);
        employee.setSubjectSet(Set.of(subject));

        employeeDroRequest = new EmployeeDroRequest();
        employeeDroRequest.setId(10L);
        employeeDroRequest.setPosition("Teacher");
        employeeDroRequest.setSalary(5000);
        employeeDroRequest.setUserId(1L);
        employeeDroRequest.setSubjectSet(Set.of(100L));

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        lenient().when(userMapper.toUserDto(user)).thenReturn(userDto);
        lenient().when(userService.getEntityById(1L)).thenReturn(user);
        lenient().when(subjectService.findByIdEntity(100L)).thenReturn(subject);
    }

    @Test
    void testToEmployeeDto() {
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);

        assertNotNull(employeeDto);
        assertEquals(10L, employeeDto.getId());
        assertEquals("Teacher", employeeDto.getPosition());
        assertEquals(5000, employeeDto.getSalary());
        assertNotNull(employeeDto.getUserDto());
        assertEquals("John", employeeDto.getUserDto().getFirstName());
        assertEquals(1, employeeDto.getSubjectSet().size());
        assertTrue(employeeDto.getSubjectSet().contains("Mathematics"));
    }

    @Test
    void testToEmployeeEntity() {
        Employee mappedEmployee = employeeMapper.toEmployeeEntity(employeeDroRequest);

        assertNotNull(mappedEmployee);
        assertEquals(10L, mappedEmployee.getId());
        assertEquals("Teacher", mappedEmployee.getPosition());
        assertEquals(5000, mappedEmployee.getSalary());
        assertEquals(user, mappedEmployee.getUser());
        assertEquals(1, mappedEmployee.getSubjectSet().size());
        assertTrue(mappedEmployee.getSubjectSet().contains(subject));
    }

    @Test
    void testToEmployeeDtoList() {
        List<EmployeeDto> employeeDtoList = employeeMapper.toEmployeeDtoList(List.of(employee));

        assertNotNull(employeeDtoList);
        assertEquals(1, employeeDtoList.size());
        assertEquals("Teacher", employeeDtoList.get(0).getPosition());
    }

    @Test
    void testToEmployeeDtoSet() {
        Set<EmployeeDto> employeeDtoSet = employeeMapper.toEmployeeDtoSet(Set.of(employee));

        assertNotNull(employeeDtoSet);
        assertEquals(1, employeeDtoSet.size());
    }
}
