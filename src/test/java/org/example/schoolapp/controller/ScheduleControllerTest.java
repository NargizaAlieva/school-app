package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.service.ScheduleService;
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

class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    void getScheduleById_ShouldReturnSchedule() throws Exception {
        Long scheduleId = 1L;
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(1L)
                .dueTime("10.00-11.00")
                .build();
        when(scheduleService.getScheduleById(scheduleId)).thenReturn(scheduleDto);

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-id/" + scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Schedule with Id: " + scheduleId))
                .andExpect(jsonPath("$.data.id").value(scheduleId));
    }

    @Test
    void getAllSchedule_ShouldReturnListOfSchedules() throws Exception {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(2L)
                .dueTime("10.00-11.00")
                .build();
        List<ScheduleDto> schedules = List.of(scheduleDto);
        when(scheduleService.getAllSchedule()).thenReturn(schedules);

        mockMvc.perform(get("/ap1/v1/schedule/get-all-schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Schedules."))
                .andExpect(jsonPath("$.data[0].id").value(2));
    }

    @Test
    void createSchedule_ShouldReturnCreatedSchedule() throws Exception {
        ScheduleDtoRequest request = ScheduleDtoRequest.builder()
                .dayOfWeek("Monday")
                .dueTime("10.00-11.00")
                .gradeId(1L)
                .build();
        ScheduleDto responseDto = ScheduleDto.builder()
                .id(2L)
                .dueTime("10.00-11.00")
                .build();
        when(scheduleService.createSchedule(any(ScheduleDtoRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/ap1/v1/schedule/create-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dueTime\":\"10.00-11.00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Schedule."))
                .andExpect(jsonPath("$.data.dueTime").value("10.00-11.00"));
    }
}
