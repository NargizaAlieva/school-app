package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.service.ScheduleService;
import org.example.schoolapp.util.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getScheduleById() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getScheduleById(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Schedule with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data.quarter").value(1))
                .andExpect(jsonPath("$.data.dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data.schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data.subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data.teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data.gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data.isApprove").value(true))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void getAllSchedules() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllSchedule()).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-all-schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Schedules."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(true))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllSchedules_EmptyList() throws Exception {
        when(scheduleService.getAllSchedule()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-all-schedule"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Schedules found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllActiveSchedules() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllActiveSchedule()).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-all-active-schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Schedules."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(true))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllActiveSchedules_EmptyList() throws Exception {
        when(scheduleService.getAllActiveSchedule()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-all-active-schedule"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No active Schedules found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllUnapprovedSchedules() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(false);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllUnApprovedSchedule()).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-all-unapproved-schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all unapproved Schedules."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(false))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllUnapprovedSchedules_EmptyList() throws Exception {
        when(scheduleService.getAllUnApprovedSchedule()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-all-unapproved-schedule"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No unapproved Schedules found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getScheduleByTeacherId() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllScheduleByTeacher(anyLong())).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-teacher-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Schedules with teacherId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(true))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getScheduleByTeacherId_EmptyList() throws Exception {
        when(scheduleService.getAllScheduleByTeacher(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-teacher-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Schedules with teacherId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getScheduleByGradeId() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllScheduleByGrade(anyLong())).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-grade-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Schedules with gradeId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(true))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getScheduleByGradeId_EmptyList() throws Exception {
        when(scheduleService.getAllScheduleByGrade(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-grade-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Schedules with gradeId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getScheduleByStudentId() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllScheduleByStudent(anyLong())).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-student-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Schedules with studentId: 1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(true))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getScheduleByStudentId_EmptyList() throws Exception {
        when(scheduleService.getAllScheduleByStudent(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-student-id/1"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Schedules with studentId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getScheduleByYear() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.getAllScheduleByYear(anyString())).thenReturn(List.of(scheduleDto));

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-year/2023-2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Schedules with year: 2023-2024"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data[0].quarter").value(1))
                .andExpect(jsonPath("$.data[0].dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data[0].schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data[0].subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].isApprove").value(true))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getScheduleByYear_EmptyList() throws Exception {
        when(scheduleService.getAllScheduleByYear(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/schedule/get-schedule-by-year/2023-2024"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Schedules with year: 2023-2024 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createSchedule() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.createSchedule(any(ScheduleDtoRequest.class))).thenReturn(scheduleDto);

        mockMvc.perform(post("/ap1/v1/schedule/create-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dayOfWeek\": \"MONDAY\", \"quarter\": 1, \"dueTime\": \"09:00-10:00\", \"schoolYear\": \"2023-2024\", \"subjectId\": 1, \"teacherId\": 1, \"gradeId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Schedule."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data.quarter").value(1))
                .andExpect(jsonPath("$.data.dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data.schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data.subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data.teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data.gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data.isApprove").value(true))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void updateSchedule() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.updateSchedule(any(ScheduleDtoRequest.class))).thenReturn(scheduleDto);

        mockMvc.perform(put("/ap1/v1/schedule/update-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"dayOfWeek\": \"MONDAY\", \"quarter\": 1, \"dueTime\": \"09:00-10:00\", \"schoolYear\": \"2023-2024\", \"subjectId\": 1, \"teacherId\": 1, \"gradeId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Schedule with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data.quarter").value(1))
                .andExpect(jsonPath("$.data.dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data.schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data.subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data.teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data.gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data.isApprove").value(true))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void deleteSchedule() throws Exception {
        mockMvc.perform(delete("/ap1/v1/schedule/delete-schedule/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Schedule with id: 1"));
    }

    @Test
    void restoreSchedule() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(true);
        scheduleDto.setIsActive(true);

        when(scheduleService.restoreSchedule(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(put("/ap1/v1/schedule/restore-schedule/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Schedule with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data.quarter").value(1))
                .andExpect(jsonPath("$.data.dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data.schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data.subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data.teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data.gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data.isApprove").value(true))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void disapproveSchedule() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);
        scheduleDto.setDayOfWeek(DaysOfWeek.MONDAY);
        scheduleDto.setQuarter(1);
        scheduleDto.setDueTime("09:00-10:00");
        scheduleDto.setSchoolYear("2023-2024");
        scheduleDto.setSubjectId(1L);
        scheduleDto.setSubjectTitle("Mathematics");
        scheduleDto.setTeacherName("John Doe");
        scheduleDto.setGradeId(1L);
        scheduleDto.setGradeName("Grade 10");
        scheduleDto.setIsApprove(false);
        scheduleDto.setIsActive(true);

        when(scheduleService.disapproveSchedule(anyLong())).thenReturn(scheduleDto);

        mockMvc.perform(put("/ap1/v1/schedule/disapprove-schedule/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully disapprove Schedule with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.data.quarter").value(1))
                .andExpect(jsonPath("$.data.dueTime").value("09:00-10:00"))
                .andExpect(jsonPath("$.data.schoolYear").value("2023-2024"))
                .andExpect(jsonPath("$.data.subjectTitle").value("Mathematics"))
                .andExpect(jsonPath("$.data.teacherName").value("John Doe"))
                .andExpect(jsonPath("$.data.gradeName").value("Grade 10"))
                .andExpect(jsonPath("$.data.isApprove").value(false))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }
}