package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.service.LessonService;
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

class LessonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getLessonById() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getLessonById(anyLong())).thenReturn(lessonDto);

        mockMvc.perform(get("/ap1/v1/lesson/get-lesson-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Lesson with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data.homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessons() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLesson()).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessons_EmptyList() throws Exception {
        when(lessonService.getAllLesson()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Lessons found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllLessonsByTeacherId() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLessonByTeacherId(anyLong())).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-teacher-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons with teacherId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessonsByTeacherId_EmptyList() throws Exception {
        when(lessonService.getAllLessonByTeacherId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-teacher-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Lessons with teacherId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllLessonsByGradeId() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLessonByGradeId(anyLong())).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-grade-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons with gradeId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessonsByGradeId_EmptyList() throws Exception {
        when(lessonService.getAllLessonByGradeId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-grade-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Lessons with gradeId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllLessonsBySubjectId() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLessonBySubjectId(anyLong())).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-subject-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons with subjectId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessonsBySubjectId_EmptyList() throws Exception {
        when(lessonService.getAllLessonBySubjectId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-subject-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Lessons with subjectId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllLessonsByYear() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLessonByYear(anyString())).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-year/2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons with year: 2023"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessonsByYear_EmptyList() throws Exception {
        when(lessonService.getAllLessonByYear(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-year/2023"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Lessons with year: 2023 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllLessonsByQuarter() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLessonByQuarter(anyInt())).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-quarter/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons with quarter: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessonsByQuarter_EmptyList() throws Exception {
        when(lessonService.getAllLessonByQuarter(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-quarter/1"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Lessons with quarter: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllLessonsBySubjectIdAndGradeId() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.getAllLessonBySubjectQuarter(anyLong(), anyLong())).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-subjectId-gradeId/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons with subjectId: 1 and gradeId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data[0].homework").value("Solve exercises 1-5"));
    }

    @Test
    void getAllLessonsBySubjectIdAndGradeId_EmptyList() throws Exception {
        when(lessonService.getAllLessonBySubjectQuarter(anyLong(), anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson-by-subjectId-gradeId/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Lessons with subjectId: 1 and gradeId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createLesson() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-5");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.createLesson(any(LessonDtoRequest.class))).thenReturn(lessonDto);

        mockMvc.perform(post("/ap1/v1/lesson/create-lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"topic\": \"Algebra Basics\", \"homework\": \"Solve exercises 1-5\", \"scheduleId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Lesson."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.topic").value("Algebra Basics"))
                .andExpect(jsonPath("$.data.homework").value("Solve exercises 1-5"));
    }

    @Test
    void updateLesson() throws Exception {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setTopic("Updated Algebra Basics");
        lessonDto.setHomework("Solve exercises 1-10");
        lessonDto.setCreationDate(LocalDateTime.now());
        lessonDto.setSchedule(new ScheduleDto());

        when(lessonService.updateLesson(any(LessonDtoRequest.class))).thenReturn(lessonDto);

        mockMvc.perform(put("/ap1/v1/lesson/update-lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"topic\": \"Updated Algebra Basics\", \"homework\": \"Solve exercises 1-10\", \"scheduleId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Lesson with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.topic").value("Updated Algebra Basics"))
                .andExpect(jsonPath("$.data.homework").value("Solve exercises 1-10"));
    }
}