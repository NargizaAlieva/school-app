package org.example.schoolapp.controller;

import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.entity.EmployeeService;
import org.example.schoolapp.service.entity.GradeService;
import org.example.schoolapp.service.entity.UserService;
import org.example.schoolapp.service.role.ParentRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentControllerTest {

    @Mock
    private ParentRoleService parentRoleService;
    private UserService userService;
    private EmployeeService employeeService;
    private GradeService gradeService;

    @InjectMocks
    private ParentController parentController;

    private StudentDtoRequest studentRequest;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        studentRequest = new StudentDtoRequest();

        studentDto = new StudentDto();
        studentDto.setId(1L);
    }

    @Test
    void createStudent_Success() {
        when(parentRoleService.createStudent(any(StudentDtoRequest.class))).thenReturn(studentDto);

        ResponseEntity<Response> response = parentController.createStudent(studentRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully created Student.", response.getBody().getMessage());
        assertEquals(studentDto, response.getBody().getData());

        verify(parentRoleService, times(1)).createStudent(studentRequest);
    }

    @Test
    void getChildList_NotFound() {
        when(parentRoleService.getChildList()).thenThrow(new RuntimeException("No children found"));

        ResponseEntity<Response> response = parentController.getChildList();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Children not found"));
        assertNull(response.getBody().getData());

        verify(parentRoleService, times(1)).getChildList();
    }

    @Test
    void getStudentSchedule_NotFound() {
        Long childId = 1L;
        when(parentRoleService.getStudentSchedule(childId))
                .thenThrow(new RuntimeException("Child not found"));

        ResponseEntity<Response> response = parentController.getStudentSchedule(childId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Couldn't find"));
        assertNull(response.getBody().getData());

        verify(parentRoleService, times(1)).getStudentSchedule(childId);
    }

    @Test
    void deleteUser_Success() {
        Long childId = 1L;
        doNothing().when(parentRoleService).leaveSchool(childId);

        ResponseEntity<String> response = parentController.deleteUser(childId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Student successfully.", response.getBody());

        verify(parentRoleService, times(1)).leaveSchool(childId);
    }

    @Test
    void deleteUser_NotFound() {
        Long childId = 1L;
        doThrow(new RuntimeException("Student not found")).when(parentRoleService).leaveSchool(childId);

        ResponseEntity<String> response = parentController.deleteUser(childId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Failed to delete"));

        verify(parentRoleService, times(1)).leaveSchool(childId);
    }
}