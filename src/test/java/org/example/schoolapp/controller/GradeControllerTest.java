package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.service.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();
    }

    private GradeDto buildGradeDto() {
        return GradeDto.builder()
                .id(1L)
                .title("Grade 10")
                .isActive(true)
                .build();
    }

    private GradeDtoRequest buildGradeDtoRequest() {
        return GradeDtoRequest.builder()
                .id(1L)
                .title("Grade 10")
                .isActive(true)
                .build();
    }

    @Test
    void getGradeById_Success() throws Exception {
        when(gradeService.getById(1L)).thenReturn(buildGradeDto());
        mockMvc.perform(get("/ap1/v1/grade/get-grade-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Grade with Id: 1"));
    }

    @Test
    void getAllGrade_Success() throws Exception {
        when(gradeService.getAllGrade()).thenReturn(List.of(buildGradeDto()));
        mockMvc.perform(get("/ap1/v1/grade/get-all-grade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void createGrade_Success() throws Exception {
        when(gradeService.createGrade(any(GradeDtoRequest.class))).thenReturn(buildGradeDto());
        mockMvc.perform(post("/ap1/v1/grade/create-grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Grade 10\",\"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Grade."));
    }

    @Test
    void deleteGrade_Success() throws Exception {
        mockMvc.perform(delete("/ap1/v1/grade/delete-grade/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Grade with id: 1"));
    }
}