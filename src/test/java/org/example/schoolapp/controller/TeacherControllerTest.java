package org.example.schoolapp.controller;

import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.service.role.TeacherService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private LessonDtoRequest lessonRequest;
    private LessonDto lessonDto;
    private MarkDtoRequest markRequest;
    private MarkDto markDto;

    @BeforeEach
    void setUp() {
        lessonRequest = new LessonDtoRequest();

        lessonDto = new LessonDto();
        lessonDto.setId(1L);

        markRequest = new MarkDtoRequest();

        markDto = new MarkDto();
        markDto.setId(1L);
    }


    @Test
    void getTeacherSchedule_NotFound() {
        when(teacherService.getTeacherSchedule())
                .thenThrow(new RuntimeException("Schedule not found"));

        ResponseEntity<Response> response = teacherController.getTeacherSchedule();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Couldn't found"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).getTeacherSchedule();
    }

    @Test
    void getTeacherSubjectList_NotFound() {
        when(teacherService.getTeacherSubjectList())
                .thenThrow(new RuntimeException("Subjects not found"));

        ResponseEntity<Response> response = teacherController.getTeacherSubjectList();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Couldn't found"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).getTeacherSubjectList();
    }

    // Lesson Tests
    @Test
    void createLesson_Success() {
        when(teacherService.createLesson(any(LessonDtoRequest.class))).thenReturn(lessonDto);

        ResponseEntity<Response> response = teacherController.createLesson(lessonRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully created Lesson.", response.getBody().getMessage());
        assertEquals(lessonDto, response.getBody().getData());
        verify(teacherService, times(1)).createLesson(lessonRequest);
    }

    @Test
    void createLesson_BadRequest() {
        when(teacherService.createLesson(any(LessonDtoRequest.class)))
                .thenThrow(new RuntimeException("Validation error"));

        ResponseEntity<Response> response = teacherController.createLesson(lessonRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Lesson is not saved"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).createLesson(lessonRequest);
    }

    @Test
    void updateLesson_Success() {
        when(teacherService.updateLesson(any(LessonDtoRequest.class))).thenReturn(lessonDto);

        ResponseEntity<Response> response = teacherController.updateLesson(lessonRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully updated Lesson.", response.getBody().getMessage());
        assertEquals(lessonDto, response.getBody().getData());
        verify(teacherService, times(1)).updateLesson(lessonRequest);
    }

    @Test
    void updateLesson_BadRequest() {
        when(teacherService.updateLesson(any(LessonDtoRequest.class)))
                .thenThrow(new RuntimeException("Update error"));

        ResponseEntity<Response> response = teacherController.updateLesson(lessonRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Lesson is not updated"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).updateLesson(lessonRequest);
    }

    // Mark Tests
    @Test
    void createMark_Success() {
        when(teacherService.createMark(any(MarkDtoRequest.class))).thenReturn(markDto);

        ResponseEntity<Response> response = teacherController.createMark(markRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully created Mark.", response.getBody().getMessage());
        assertEquals(markDto, response.getBody().getData());
        verify(teacherService, times(1)).createMark(markRequest);
    }

    @Test
    void createMark_BadRequest() {
        when(teacherService.createMark(any(MarkDtoRequest.class)))
                .thenThrow(new RuntimeException("Validation error"));

        ResponseEntity<Response> response = teacherController.createMark(markRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Mark is not saved"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).createMark(markRequest);
    }

    @Test
    void updateMark_Success() {
        when(teacherService.updateMark(any(MarkDtoRequest.class))).thenReturn(markDto);

        ResponseEntity<Response> response = teacherController.updateMark(markRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully updated Mark.", response.getBody().getMessage());
        assertEquals(markDto, response.getBody().getData());
        verify(teacherService, times(1)).updateMark(markRequest);
    }

    @Test
    void updateMark_BadRequest() {
        when(teacherService.updateMark(any(MarkDtoRequest.class)))
                .thenThrow(new RuntimeException("Update error"));

        ResponseEntity<Response> response = teacherController.updateMark(markRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Mark is not updated"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).updateMark(markRequest);
    }

    @Test
    void getAllGrade_NotFound() {
        when(teacherService.getAllGrade())
                .thenThrow(new RuntimeException("Grades not found"));

        ResponseEntity<Response> response = teacherController.getAllGrade();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Couldn't found"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).getAllGrade();
    }

    @Test
    void getAllStudentByGrade_NotFound() {
        Long gradeId = 1L;
        when(teacherService.getAllStudentByGrade(gradeId))
                .thenThrow(new RuntimeException("Students not found"));

        ResponseEntity<Response> response = teacherController.getAllStudentByGrade(gradeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Couldn't found"));
        assertNull(response.getBody().getData());
        verify(teacherService, times(1)).getAllStudentByGrade(gradeId);
    }
}
