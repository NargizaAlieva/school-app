package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.service.MarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
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
        mockMvc = MockMvcBuilders.standaloneSetup(markController).build();
    }

    @Test
    void testGetMarkById() throws Exception {
        MarkDto markDto = MarkDto.builder().id(1L).mark(95).studentId(10L).build();
        when(markService.getMarkById(1L)).thenReturn(markDto);

        mockMvc.perform(get("/ap1/v1/mark/get-mark-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Mark with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.mark").value(95));
    }

    @Test
    void testGetAllMark() throws Exception {
        List<MarkDto> marks = List.of(
                MarkDto.builder().id(1L).mark(90).studentId(10L).build(),
                MarkDto.builder().id(2L).mark(85).studentId(11L).build()
        );
        when(markService.getAllMark()).thenReturn(marks);

        mockMvc.perform(get("/ap1/v1/mark/get-all-mark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    void testCreateMark() throws Exception {
        MarkDtoRequest request = MarkDtoRequest.builder().mark(88).studentId(12L).build();
        MarkDto responseDto = MarkDto.builder().id(3L).mark(88).studentId(12L).build();
        when(markService.createMark(any())).thenReturn(responseDto);

        mockMvc.perform(post("/ap1/v1/mark/create-mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mark\":88,\"studentId\":12}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Mark."))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.mark").value(88));
    }
}