package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.entity.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GradeMapperTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private GradeMapper gradeMapper;

    private Grade grade;
    private GradeDtoRequest gradeDtoRequest;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = User.builder()
                .firstName("Sara")
                .lastName("Doe")
                .middleName("M.")
                .email("sara@gmail.com")
                .build();

        employee = Employee.builder()
                .user(user)
                .build();

        grade = Grade.builder()
                .id(1L)
                .title("10A")
                .classTeacher(employee)
                .studentSet(Set.of())
                .isActive(true)
                .build();

        gradeDtoRequest = GradeDtoRequest.builder()
                .id(1L)
                .title("10A")
                .classTeacherId(1L)
                .isActive(true)
                .build();
    }

    @Test
    void testToGradeDto() {
        GradeDto gradeDto = gradeMapper.toGradeDto(grade);

        assertNotNull(gradeDto);
        assertEquals(grade.getId(), gradeDto.getId());
        assertEquals(grade.getTitle(), gradeDto.getTitle());
        assertEquals("Sara Doe M.", gradeDto.getClassTeacher());
        assertEquals(0, gradeDto.getStudentCount());
        assertEquals(grade.getCreationDate(), gradeDto.getCreationDate());
        assertEquals(grade.getIsActive(), gradeDto.getIsActive());
    }

    @Test
    void testToGradeEntity() {
        when(employeeService.findByIdEntity(1L)).thenReturn(employee);

        Grade entity = gradeMapper.toGradeEntity(gradeDtoRequest);

        assertNotNull(entity);
        assertEquals(gradeDtoRequest.getId(), entity.getId());
        assertEquals(gradeDtoRequest.getTitle(), entity.getTitle());
        assertEquals(employee, entity.getClassTeacher());
        assertNotNull(entity.getCreationDate());
        assertEquals(gradeDtoRequest.getIsActive(), entity.getIsActive());
    }
}