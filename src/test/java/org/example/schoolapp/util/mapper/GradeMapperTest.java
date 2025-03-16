package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("M.");

        employee = new Employee();
        employee.setUser(user);

        grade = new Grade();
        grade.setId(1L);
        grade.setTitle("10A");
        grade.setClassTeacher(employee);
        grade.setStudentSet(Set.of());
        grade.setCreationDate(LocalDateTime.now());
        grade.setIsActive(true);

        gradeDtoRequest = new GradeDtoRequest();
        gradeDtoRequest.setId(1L);
        gradeDtoRequest.setTitle("10A");
        gradeDtoRequest.setClassTeacherId(1L);
        gradeDtoRequest.setCreationDate(null);
        gradeDtoRequest.setIsActive(true);
    }

    @Test
    void testToGradeDto() {
        GradeDto gradeDto = gradeMapper.toGradeDto(grade);

        assertNotNull(gradeDto);
        assertEquals(grade.getId(), gradeDto.getId());
        assertEquals(grade.getTitle(), gradeDto.getTitle());
        assertEquals("John Doe M.", gradeDto.getClassTeacher());
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