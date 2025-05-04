package org.example.schoolapp.controller;

import org.example.schoolapp.dto.Response;
import org.example.schoolapp.service.role.StudentRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentRoleService studentRoleService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getAllMark_NotFound() {
        when(studentRoleService.getAllMark()).thenThrow(new RuntimeException("No marks found"));

        ResponseEntity<Response> response = studentController.getAllMark();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Marks not found"));
        assertNull(response.getBody().getData());
        verify(studentRoleService, times(1)).getAllMark();
    }

    @Test
    void getAllMarkBySubjectQuarter_NotFound() {
        Long subjectId = 1L;
        when(studentRoleService.getAvgMarkBySubjectGradeStudent(subjectId))
                .thenThrow(new RuntimeException("Subject not found"));

        ResponseEntity<Response> response = studentController.getAllMarkBySubjectQuarter(subjectId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Marks not found"));
        assertNull(response.getBody().getData());
        verify(studentRoleService, times(1)).getAvgMarkBySubjectGradeStudent(subjectId);
    }

    @Test
    void getAvgMarkByGradeStudent_NotFound() {
        when(studentRoleService.getAvgMarkByGradeStudent())
                .thenThrow(new RuntimeException("No marks for year"));

        ResponseEntity<Response> response = studentController.getAvgMarkByGradeStudent();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Marks not found"));
        assertNull(response.getBody().getData());
        verify(studentRoleService, times(1)).getAvgMarkByGradeStudent();
    }

    @Test
    void getStudentSchedule_NotFound() {
        when(studentRoleService.getStudentSchedule())
                .thenThrow(new RuntimeException("No schedule found"));

        ResponseEntity<Response> response = studentController.getStudentSchedule();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Schedules is not found"));
        assertNull(response.getBody().getData());
        verify(studentRoleService, times(1)).getStudentSchedule();
    }

    @Test
    void getAllLessonByGrade_NotFound() {
        when(studentRoleService.getAllLessonByGrade())
                .thenThrow(new RuntimeException("No lessons found"));

        ResponseEntity<Response> response = studentController.getAllLessonByGrade();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Lessons topic is not found"));
        assertNull(response.getBody().getData());
        verify(studentRoleService, times(1)).getAllLessonByGrade();
    }

    @Test
    void getClassmates_NotFound() {
        when(studentRoleService.getClassmates())
                .thenThrow(new RuntimeException("No classmates found"));

        ResponseEntity<Response> response = studentController.getClassmates();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Classmates is not found"));
        assertNull(response.getBody().getData());
        verify(studentRoleService, times(1)).getClassmates();
    }
}