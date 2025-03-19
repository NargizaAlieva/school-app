package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.util.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getGradeById() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.getById(anyLong())).thenReturn(gradeDto);

        mockMvc.perform(get("/ap1/v1/grade/get-grade-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Grade with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Grade 10"))
                .andExpect(jsonPath("$.data.classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data.studentCount").value(30))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void getAllGrades() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.getAllGrade()).thenReturn(List.of(gradeDto));

        mockMvc.perform(get("/ap1/v1/grade/get-all-grade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Grades."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data[0].studentCount").value(30))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllGrades_EmptyList() throws Exception {
        when(gradeService.getAllGrade()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/grade/get-all-grade"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Grades found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getAllActiveGrades() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.getAllActiveGrade()).thenReturn(List.of(gradeDto));

        mockMvc.perform(get("/ap1/v1/grade/get-all-active-grade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Grades."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data[0].studentCount").value(30))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllActiveGrades_EmptyList() throws Exception {
        when(gradeService.getAllActiveGrade()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/grade/get-all-active-grade"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No active Grades found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getGradeByTeacherId() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.getAllGradeByTeacherId(anyLong())).thenReturn(List.of(gradeDto));

        mockMvc.perform(get("/ap1/v1/grade/get-grade-by-teacherId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Grades with home teacher with id: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data[0].studentCount").value(30))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getGradeByTeacherId_EmptyList() throws Exception {
        when(gradeService.getAllGradeByTeacherId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/grade/get-grade-by-teacherId/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Grades with home teacher with id: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getGradeByStudentId() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.getGradeByStudentId(anyLong())).thenReturn(gradeDto);

        mockMvc.perform(get("/ap1/v1/grade/get-grade-by-studentId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Grade with student by id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Grade 10"))
                .andExpect(jsonPath("$.data.classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data.studentCount").value(30))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void createGrade() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.createGrade(any(GradeDtoRequest.class))).thenReturn(gradeDto);

        mockMvc.perform(post("/ap1/v1/grade/create-grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Grade 10\", \"classTeacherId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Grade."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Grade 10"))
                .andExpect(jsonPath("$.data.classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data.studentCount").value(30))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void updateGrade() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Updated Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.updateGrade(any(GradeDtoRequest.class))).thenReturn(gradeDto);

        mockMvc.perform(put("/ap1/v1/grade/update-grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"Updated Grade 10\", \"classTeacherId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Grade with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Updated Grade 10"))
                .andExpect(jsonPath("$.data.classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data.studentCount").value(30))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void deleteGrade() throws Exception {
        mockMvc.perform(delete("/ap1/v1/grade/delete-grade/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Grade with id: 1"));
    }

    @Test
    void restoreGrade() throws Exception {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(1L);
        gradeDto.setTitle("Restored Grade 10");
        gradeDto.setCreationDate(LocalDateTime.now());
        gradeDto.setClassTeacher("John Doe");
        gradeDto.setStudentCount(30);
        gradeDto.setIsActive(true);

        when(gradeService.restoreGrade(anyLong())).thenReturn(gradeDto);

        mockMvc.perform(put("/ap1/v1/grade/restore-grade/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Grade with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Restored Grade 10"))
                .andExpect(jsonPath("$.data.classTeacher").value("John Doe"))
                .andExpect(jsonPath("$.data.studentCount").value(30))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }
}