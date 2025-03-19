package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.ScheduleController;
import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.service.ScheduleService;
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

import java.util.Collections;
import java.util.List;

import static org.example.schoolapp.enums.DaysOfWeek.MONDAY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    private ScheduleDto scheduleDto;
    private ScheduleDtoRequest scheduleDtoRequest;

    @BeforeEach
    void setUp() {
        scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(MONDAY);
        scheduleDto.setQuarter(1);

        scheduleDtoRequest = new ScheduleDtoRequest();
        scheduleDtoRequest.setDayOfWeek("MONDAY");
    }

    @Test
    void getScheduleById_ShouldReturnSchedule() throws Exception {
        Mockito.when(scheduleService.getScheduleById(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-schedule-by-id/{scheduleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved Schedule with Id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void getAllSchedules_ShouldReturnListOfSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllSchedule()).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-all-schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Schedules."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getAllActiveSchedules_ShouldReturnListOfActiveSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllActiveSchedule()).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-all-active-schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all active Schedules."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getAllUnapprovedSchedules_ShouldReturnListOfUnapprovedSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllUnApprovedSchedule()).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-all-unapproved-schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all unapproved Schedules."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getSchedulesByTeacherId_ShouldReturnListOfSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllScheduleByTeacher(anyLong())).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-schedule-by-teacher-id/{teacherId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Schedules with teacherId: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getSchedulesByGradeId_ShouldReturnListOfSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllScheduleByGrade(anyLong())).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-schedule-by-grade-id/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Schedules with gradeId: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getSchedulesByStudentId_ShouldReturnListOfSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllScheduleByStudent(anyLong())).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-schedule-by-student-id/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Schedules with studentId: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getSchedulesByYear_ShouldReturnListOfSchedules() throws Exception {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        Mockito.when(scheduleService.getAllScheduleByYear(anyString())).thenReturn(schedules);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/schedule/get-schedule-by-year/{year}", "2023")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Schedules with year: 2023"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void createSchedule_ShouldReturnCreatedSchedule() throws Exception {
        Mockito.when(scheduleService.createSchedule(any(ScheduleDtoRequest.class))).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/schedule/create-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created Schedule."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void updateSchedule_ShouldReturnUpdatedSchedule() throws Exception {
        Mockito.when(scheduleService.updateSchedule(any(ScheduleDtoRequest.class))).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/schedule/update-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated Schedule with id: null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void deleteSchedule_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(scheduleService).deleteSchedule(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/schedule/delete-schedule/{scheduleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted Schedule with id: 1"));
    }

    @Test
    void restoreSchedule_ShouldReturnRestoredSchedule() throws Exception {
        Mockito.when(scheduleService.restoreSchedule(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/schedule/restore-schedule/{scheduleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully restored Schedule with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void approveSchedule_ShouldReturnApprovedSchedule() throws Exception {
        Mockito.when(scheduleService.approveSchedule(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/schedule/approve-schedule/{scheduleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully approve Schedule with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void disapproveSchedule_ShouldReturnDisapprovedSchedule() throws Exception {
        Mockito.when(scheduleService.disapproveSchedule(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/schedule/disapprove-schedule/{scheduleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully disapprove Schedule with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }
}