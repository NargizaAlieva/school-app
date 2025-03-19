package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.MarkController;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.service.MarkService;
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

@WebMvcTest(MarkController.class)
public class MarkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkService markService;

    @Autowired
    private ObjectMapper objectMapper;

    private MarkDto markDto;
    private MarkDtoRequest markDtoRequest;

    @BeforeEach
    void setUp() {
        markDto = MarkDto.builder()
                .id(1L)
                .mark(85)
                .studentId(1L)
                .studentName("John Doe")
                .gottenDate(LocalDateTime.now())
                .build();

        markDtoRequest = MarkDtoRequest.builder()
                .id(1L)
                .mark(85)
                .studentId(1L)
                .lessonId(1L)
                .build();
    }

    @Test
    void testGetMarkById() throws Exception {
        Mockito.when(markService.getMarkById(anyLong())).thenReturn(markDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/mark/get-mark-by-id/{markId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mark").value(85));
    }

    @Test
    void testGetAllMark() throws Exception {
        List<MarkDto> marks = Collections.singletonList(markDto);
        Mockito.when(markService.getAllMark()).thenReturn(marks);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/mark/get-all-mark")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].mark").value(85));
    }

    @Test
    void testGetAllMarkByStudentId() throws Exception {
        List<MarkDto> marks = Collections.singletonList(markDto);
        Mockito.when(markService.getAllMarkByStudent(anyLong())).thenReturn(marks);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/mark/get-all-mark-by-student-id/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].mark").value(85));
    }

    @Test
    void testCreateMark() throws Exception {
        Mockito.when(markService.createMark(any(MarkDtoRequest.class))).thenReturn(markDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/mark/create-mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(markDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mark").value(85));
    }

    @Test
    void testUpdateMark() throws Exception {
        Mockito.when(markService.updateMark(any(MarkDtoRequest.class))).thenReturn(markDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/mark/update-mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(markDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mark").value(85));
    }

    @Test
    void testGetAvgMarkByStudentId() throws Exception {
        Mockito.when(markService.getAverageMarkByStudentId(anyLong())).thenReturn(85.0);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/mark/get-avg-mark-by-student-id/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(85.0));
    }

    @Test
    void testGetAvgMarkByStudentIdAndSubjectId() throws Exception {
        Mockito.when(markService.getAverageMarkByStudentIdAndSubjectId(anyLong(), anyLong())).thenReturn(85.0);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/mark/get-avg-mark-by-studentId-subjectId/{studentId}/{subjectId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(85.0));
    }
}