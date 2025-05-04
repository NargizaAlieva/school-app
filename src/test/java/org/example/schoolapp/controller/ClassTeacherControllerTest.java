package org.example.schoolapp.controller;

import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.service.role.ClassTeacherService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassTeacherControllerTest {

    @Mock
    private ClassTeacherService classTeacherService;

    @InjectMocks
    private ClassTeacherController classTeacherController;

    private MarkDtoRequest markRequest;
    private MarkDto markDto;

    @BeforeEach
    void setUp() {
        markRequest = new MarkDtoRequest();
        markDto = new MarkDto();
    }

    @Test
    void studentsFromClass_ShouldReturnStudents_WhenSuccessful() {
        List<StudentDto> students = Collections.singletonList(new StudentDto());
        when(classTeacherService.getAllStudentsFromClass()).thenReturn(students);

        ResponseEntity<Response> response = classTeacherController.studentsFromClass();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully got all class Student from home grades.",
                response.getBody().getMessage());
        assertEquals(students, response.getBody().getData());
        verify(classTeacherService).getAllStudentsFromClass();
    }

    @Test
    void studentsFromClass_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        when(classTeacherService.getAllStudentsFromClass())
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Response> response = classTeacherController.studentsFromClass();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Couldn't find"));
        assertNull(response.getBody().getData());
    }

    @Test
    void getAllHomeGrades_ShouldReturnInternalServerError_WhenExceptionOccurs() {
        when(classTeacherService.getTeacherGradesList())
                .thenThrow(new RuntimeException("Service unavailable"));

        ResponseEntity<Response> response = classTeacherController.getAllHomeGrades();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Couldn't find"));
        assertNull(response.getBody().getData());
    }

    @Test
    void createMark_ShouldReturnCreated_WhenSuccessful() {
        when(classTeacherService.createMark(markRequest)).thenReturn(markDto);

        ResponseEntity<Response> response = classTeacherController.createMark(markRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Successfully created Mark.", response.getBody().getMessage());
        assertEquals(markDto, response.getBody().getData());
        verify(classTeacherService).createMark(markRequest);
    }

    @Test
    void createMark_ShouldReturnBadRequest_WhenInvalidData() {
        when(classTeacherService.createMark(markRequest))
                .thenThrow(new IllegalArgumentException("Invalid mark data"));

        ResponseEntity<Response> response = classTeacherController.createMark(markRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("not saved"));
        assertNull(response.getBody().getData());
    }

    @Test
    void updateMark_ShouldReturnBadRequest_WhenInvalidData() {
        when(classTeacherService.updateMark(markRequest))
                .thenThrow(new IllegalArgumentException("Invalid mark data"));

        ResponseEntity<Response> response = classTeacherController.updateMark(markRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("not saved"));
        assertNull(response.getBody().getData());
    }
}