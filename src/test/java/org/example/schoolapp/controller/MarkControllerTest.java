package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.service.MarkService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MarkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MarkService markService;

    @InjectMocks
    private MarkController markController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(markController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getMarkById() throws Exception {
        MarkDto markDto = new MarkDto();
        markDto.setId(1L);
        markDto.setMark(85);
        markDto.setStudentId(1L);
        markDto.setStudentName("John Doe");
        markDto.setLesson(new LessonDto());
        markDto.setGottenDate(LocalDateTime.now());

        when(markService.getMarkById(anyLong())).thenReturn(markDto);

        mockMvc.perform(get("/ap1/v1/mark/get-mark-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Mark with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.mark").value(85))
                .andExpect(jsonPath("$.data.studentId").value(1L))
                .andExpect(jsonPath("$.data.studentName").value("John Doe"));
    }

    @Test
    void getAllMarks() throws Exception {
        MarkDto markDto = new MarkDto();
        markDto.setId(1L);
        markDto.setMark(85);
        markDto.setStudentId(1L);
        markDto.setStudentName("John Doe");
        markDto.setLesson(new LessonDto());
        markDto.setGottenDate(LocalDateTime.now());

        when(markService.getAllMark()).thenReturn(List.of(markDto));

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Marks."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].mark").value(85))
                .andExpect(jsonPath("$.data[0].studentId").value(1L))
                .andExpect(jsonPath("$.data[0].studentName").value("John Doe"));
    }

    @Test
    void getAllMarks_EmptyList() throws Exception {
        when(markService.getAllMark()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Marks found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getAllMarksByStudentId() throws Exception {
        MarkDto markDto = new MarkDto();
        markDto.setId(1L);
        markDto.setMark(85);
        markDto.setStudentId(1L);
        markDto.setStudentName("John Doe");
        markDto.setLesson(new LessonDto());
        markDto.setGottenDate(LocalDateTime.now());

        when(markService.getAllMarkByStudent(anyLong())).thenReturn(List.of(markDto));

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark-by-student-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Marks."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].mark").value(85))
                .andExpect(jsonPath("$.data[0].studentId").value(1L))
                .andExpect(jsonPath("$.data[0].studentName").value("John Doe"));
    }

    @Test
    void getAllMarksByStudentId_EmptyList() throws Exception {
        when(markService.getAllMarkByStudent(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark-by-student-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Marks with studentId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getMarksByStudentAndSchedule() throws Exception {
        MarkDto markDto = new MarkDto();
        markDto.setId(1L);
        markDto.setMark(85);
        markDto.setStudentId(1L);
        markDto.setStudentName("John Doe");
        markDto.setLesson(new LessonDto());
        markDto.setGottenDate(LocalDateTime.now());

        when(markService.getMarksByStudentAndSchedule(anyLong(), anyLong(), anyString(), anyInt())).thenReturn(List.of(markDto));

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark-by-studentId-subjectId-year-quarter/1/1/2023/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Marks with studentId: 1 subjectId: 1, year: 2023 quarter : 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].mark").value(85))
                .andExpect(jsonPath("$.data[0].studentId").value(1L))
                .andExpect(jsonPath("$.data[0].studentName").value("John Doe"));
    }

    @Test
    void getMarksByStudentAndSchedule_EmptyList() throws Exception {
        when(markService.getMarksByStudentAndSchedule(anyLong(), anyLong(), anyString(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark-by-studentId-subjectId-year-quarter/1/1/2023/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Marks with studentId: 1 subjectId: 1, year: 2023 quarter : 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void createMark() throws Exception {
        MarkDto markDto = new MarkDto();
        markDto.setId(1L);
        markDto.setMark(85);
        markDto.setStudentId(1L);
        markDto.setStudentName("John Doe");
        markDto.setLesson(new LessonDto());
        markDto.setGottenDate(LocalDateTime.now());

        when(markService.createMark(any(MarkDtoRequest.class))).thenReturn(markDto);

        mockMvc.perform(post("/ap1/v1/mark/create-mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mark\": 85, \"studentId\": 1, \"lessonId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Mark."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.mark").value(85))
                .andExpect(jsonPath("$.data.studentId").value(1L))
                .andExpect(jsonPath("$.data.studentName").value("John Doe"));
    }

    @Test
    void updateMark() throws Exception {
        MarkDto markDto = new MarkDto();
        markDto.setId(1L);
        markDto.setMark(90);
        markDto.setStudentId(1L);
        markDto.setStudentName("John Doe");
        markDto.setLesson(new LessonDto());
        markDto.setGottenDate(LocalDateTime.now());

        when(markService.updateMark(any(MarkDtoRequest.class))).thenReturn(markDto);

        mockMvc.perform(put("/ap1/v1/mark/update-mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"mark\": 90, \"studentId\": 1, \"lessonId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Mark with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.mark").value(90))
                .andExpect(jsonPath("$.data.studentId").value(1L))
                .andExpect(jsonPath("$.data.studentName").value("John Doe"));
    }

    @Test
    void getAvgMarkByStudentId() throws Exception {
        when(markService.getAverageMarkByStudentId(anyLong())).thenReturn(85.5);

        mockMvc.perform(get("/ap1/v1/mark/get-avg-mark-by-student-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved average Mark with studentId: 1"))
                .andExpect(jsonPath("$.data").value(85.5));
    }

    @Test
    void getAvgMarkByStudentId_NotFound() throws Exception {
        when(markService.getAverageMarkByStudentId(anyLong())).thenReturn(null);

        mockMvc.perform(get("/ap1/v1/mark/get-avg-mark-by-student-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Marks with studentId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getAvgMarkByStudentIdAndSubjectId() throws Exception {
        when(markService.getAverageMarkByStudentIdAndSubjectId(anyLong(), anyLong())).thenReturn(90.0);

        mockMvc.perform(get("/ap1/v1/mark/get-avg-mark-by-studentId-subjectId/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved average Mark with studentId: 1 and subjectId: 1"))
                .andExpect(jsonPath("$.data").value(90.0));
    }

    @Test
    void getAvgMarkByStudentIdAndSubjectId_NotFound() throws Exception {
        when(markService.getAverageMarkByStudentIdAndSubjectId(anyLong(), anyLong())).thenReturn(null);

        mockMvc.perform(get("/ap1/v1/mark/get-avg-mark-by-studentId-subjectId/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Marks with studentId: 1, subjectId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }
}