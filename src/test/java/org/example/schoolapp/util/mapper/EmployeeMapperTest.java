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
        user = User.builder()
                .id(1L)
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        subject = Subject.builder()
                .id(100L)
                .title("Mathematics")
                .build();

        employee = Employee.builder()
                .id(10L)
                .position("Teacher")
                .salary(5000)
                .user(user)
                .subjectSet(Set.of(subject))
                .build();

        employeeDroRequest = EmployeeDroRequest.builder()
                .id(10L)
                .position("Teacher")
                .salary(5000)
                .userId(1L)
                .subjectSet(Set.of(100L))
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();


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
