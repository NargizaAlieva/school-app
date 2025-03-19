package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.LessonController;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.service.LessonService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonController.class)
public class LessonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    private LessonDto lessonDto;
    private LessonDtoRequest lessonDtoRequest;

    @BeforeEach
    void setUp() {
        lessonDto = LessonDto.builder()
                .id(1L)
                .topic("Mathematics")
                .homework("Solve exercises 1-10")
                .creationDate(LocalDateTime.now())
                .build();

        lessonDtoRequest = LessonDtoRequest.builder()
                .topic("Mathematics")
                .homework("Solve exercises 1-10")
                .creationDate(LocalDateTime.now())
                .scheduleId(1L)
                .build();
    }

    @Test
    void getLessonById_ShouldReturnLesson() throws Exception {
        Mockito.when(lessonService.getLessonById(1L)).thenReturn(lessonDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/lesson/get-lesson-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Lesson with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.topic").value("Mathematics"));
    }

    @Test
    void getLessonById_ShouldReturnNotFound() throws Exception {
        Mockito.when(lessonService.getLessonById(1L)).thenThrow(new ObjectNotFoundException("Lesson not found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/lesson/get-lesson-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllLesson_ShouldReturnListOfLessons() throws Exception {
        Mockito.when(lessonService.getAllLesson()).thenReturn(Collections.singletonList(lessonDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/lesson/get-all-lesson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Lessons."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].topic").value("Mathematics"));
    }

    @Test
    void getAllLesson_ShouldReturnNotFound() throws Exception {
        Mockito.when(lessonService.getAllLesson()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/lesson/get-all-lesson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLesson_ShouldReturnCreatedLesson() throws Exception {
        Mockito.when(lessonService.createLesson(lessonDtoRequest)).thenReturn(lessonDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/lesson/create-lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Lesson."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.topic").value("Mathematics"));
    }

    @Test
    void updateLesson_ShouldReturnUpdatedLesson() throws Exception {
        Mockito.when(lessonService.updateLesson(lessonDtoRequest)).thenReturn(lessonDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/lesson/update-lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Lesson with id: null"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.topic").value("Mathematics"));
    }
}