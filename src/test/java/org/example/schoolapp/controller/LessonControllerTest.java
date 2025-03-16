package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
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
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void testGetLessonById() throws Exception {
        LessonDto lessonDto = LessonDto.builder()
                .id(1L)
                .topic("Math")
                .homework("HW")
                .build();;
        when(lessonService.getLessonById(1L)).thenReturn(lessonDto);

        mockMvc.perform(get("/ap1/v1/lesson/get-lesson-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successfully retrieved Lesson with Id: 1")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.topic", is("Math")));

        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    void testGetAllLesson() throws Exception {
        List<LessonDto> lessons = List.of(LessonDto.builder()
                .id(1L)
                .topic("Math")
                .homework("HW")
                .build());
        when(lessonService.getAllLesson()).thenReturn(lessons);

        mockMvc.perform(get("/ap1/v1/lesson/get-all-lesson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].topic", is("Math")));
    }

    @Test
    void testCreateLesson() throws Exception {
        LessonDtoRequest request = LessonDtoRequest.builder()
                .id(1L)
                .topic("Math")
                .build();
        LessonDto responseDto = LessonDto.builder()
                .id(1L)
                .topic("Math")
                .homework("HW")
                .build();
        when(lessonService.createLesson(any())).thenReturn(responseDto);

        mockMvc.perform(post("/ap1/v1/lesson/create-lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Math\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Successfully created Lesson.")))
                .andExpect(jsonPath("$.data.topic", is("Math")));
    }
}