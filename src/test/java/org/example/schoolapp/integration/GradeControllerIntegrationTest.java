package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.GradeController;
import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.service.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GradeController.class)
public class GradeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private GradeDto gradeDto;
    private GradeDtoRequest gradeDtoRequest;

    @BeforeEach
    void setUp() {
        gradeDto = GradeDto.builder()
                .id(1L)
                .title("Grade 10")
                .creationDate(LocalDateTime.now())
                .classTeacher("John Doe")
                .studentCount(30)
                .isActive(true)
                .build();

        gradeDtoRequest = GradeDtoRequest.builder()
                .id(1L)
                .title("Grade 10")
                .classTeacherId(1L)
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    @Test
    void testGetGradeById() throws Exception {
        Mockito.when(gradeService.getById(anyLong())).thenReturn(gradeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/grade/get-grade-by-id/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Grade 10"));
    }

    @Test
    void testGetAllGrade() throws Exception {
        List<GradeDto> grades = Collections.singletonList(gradeDto);
        Mockito.when(gradeService.getAllGrade()).thenReturn(grades);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/grade/get-all-grade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Grade 10"));
    }

    @Test
    void testGetAllActiveGrade() throws Exception {
        List<GradeDto> grades = Collections.singletonList(gradeDto);
        Mockito.when(gradeService.getAllActiveGrade()).thenReturn(grades);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/grade/get-all-active-grade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void testGetGradeByTeacherId() throws Exception {
        List<GradeDto> grades = Collections.singletonList(gradeDto);
        Mockito.when(gradeService.getAllGradeByTeacherId(anyLong())).thenReturn(grades);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/grade/get-grade-by-teacherId/{teacherId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].classTeacher").value("John Doe"));
    }

    @Test
    void testGetGradeByStudentId() throws Exception {
        Mockito.when(gradeService.getGradeByStudentId(anyLong())).thenReturn(gradeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/grade/get-grade-by-studentId/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Grade 10"));
    }

    @Test
    void testCreateGrade() throws Exception {
        Mockito.when(gradeService.createGrade(any(GradeDtoRequest.class))).thenReturn(gradeDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/grade/create-grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Grade 10"));
    }

    @Test
    void testUpdateGrade() throws Exception {
        Mockito.when(gradeService.updateGrade(any(GradeDtoRequest.class))).thenReturn(gradeDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/grade/update-grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Grade 10"));
    }

    @Test
    void testDeleteGrade() throws Exception {
        Mockito.doNothing().when(gradeService).deleteGrade(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/grade/delete-grade/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted Grade with id: 1"));
    }

    @Test
    void testRestoreGrade() throws Exception {
        Mockito.when(gradeService.restoreGrade(anyLong())).thenReturn(gradeDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/grade/restore-grade/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Grade 10"));
    }
}