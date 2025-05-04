package org.example.schoolapp.controller;

import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.service.role.PrincipleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrincipleControllerTest {

    @Mock
    private PrincipleService principleService;

    @InjectMocks
    private PrincipleController principleController;

    private ScheduleDtoRequest scheduleRequest;
    private ScheduleDto scheduleDto;
    private EmployeeDroRequest employeeRequest;
    private EmployeeDto employeeDto;
    private SubjectDtoRequest subjectRequest;
    private SubjectDto subjectDto;

    @BeforeEach
    void setUp() {
        scheduleRequest = new ScheduleDtoRequest();

        scheduleDto = new ScheduleDto();
        scheduleDto.setId(1L);

        employeeRequest = new EmployeeDroRequest();

        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);

        subjectRequest = new SubjectDtoRequest();

        subjectDto = new SubjectDto();
        subjectDto.setId(1L);
    }

    @Test
    void getScheduleById_Success() {
        when(principleService.getScheduleById(anyLong())).thenReturn(scheduleDto);

        ResponseEntity<Response> response = principleController.getScheduleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got Schedule with id", response.getBody().getMessage());
        assertEquals(scheduleDto, response.getBody().getData());
    }

    @Test
    void approveSchedule_Success() {
        when(principleService.approveSchedule(anyLong())).thenReturn(scheduleDto);

        ResponseEntity<Response> response = principleController.approveSchedule(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully approved Schedule with id", response.getBody().getMessage());
        assertEquals(scheduleDto, response.getBody().getData());
    }

    @Test
    void disapproveSchedule_Success() {
        when(principleService.disapproveSchedule(anyLong())).thenReturn(scheduleDto);

        ResponseEntity<Response> response = principleController.disapproveSchedule(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully disapproved Schedule with id", response.getBody().getMessage());
        assertEquals(scheduleDto, response.getBody().getData());
    }

    @Test
    void deleteSchedule_Success() {
        doNothing().when(principleService).deleteSchedule(anyLong());

        ResponseEntity<Response> response = principleController.deleteSchedule(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully deleted Schedule with id", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void restoreSchedule_Success() {
        when(principleService.restoreSchedule(anyLong())).thenReturn(scheduleDto);

        ResponseEntity<Response> response = principleController.restoreSchedule(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully restored Schedule with id", response.getBody().getMessage());
        assertEquals(scheduleDto, response.getBody().getData());
    }

    @Test
    void getAllSchedule_Success() {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        when(principleService.getAllSchedule()).thenReturn(schedules);

        ResponseEntity<Response> response = principleController.getAllSchedule();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all Schedules", response.getBody().getMessage());
        assertEquals(schedules, response.getBody().getData());
    }

    @Test
    void getAllActiveSchedule_Success() {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        when(principleService.getAllActiveSchedule()).thenReturn(schedules);

        ResponseEntity<Response> response = principleController.getAllActiveSchedule();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all active Schedules", response.getBody().getMessage());
        assertEquals(schedules, response.getBody().getData());
    }

    @Test
    void getAllActiveScheduleByYear_Success() {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        when(principleService.getAllScheduleByYear(anyString())).thenReturn(schedules);

        ResponseEntity<Response> response = principleController.getAllActiveScheduleByYear("2023");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all active Schedules by 2023", response.getBody().getMessage());
        assertEquals(schedules, response.getBody().getData());
    }

    @Test
    void getAllUnapprovedSchedule_Success() {
        List<ScheduleDto> schedules = Collections.singletonList(scheduleDto);
        when(principleService.getAllUnapprovedSchedule()).thenReturn(schedules);

        ResponseEntity<Response> response = principleController.getAllUnapprovedSchedule();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all unapproved Schedules", response.getBody().getMessage());
        assertEquals(schedules, response.getBody().getData());
    }

    @Test
    void createSchedule_Success() {
        when(principleService.createSchedule(any(ScheduleDtoRequest.class))).thenReturn(scheduleDto);

        ResponseEntity<Response> response = principleController.createSchedule(scheduleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully created Schedule.", response.getBody().getMessage());
        assertEquals(scheduleDto, response.getBody().getData());
    }

    @Test
    void updateSchedule_Success() {
        when(principleService.updateSchedule(any(ScheduleDtoRequest.class))).thenReturn(scheduleDto);

        ResponseEntity<Response> response = principleController.updateSchedule(scheduleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Schedule successfully.", response.getBody().getMessage());
        assertEquals(scheduleDto, response.getBody().getData());
    }

    @Test
    void updateEmployee_Success() {
        when(principleService.updateEmployee(any(EmployeeDroRequest.class))).thenReturn(employeeDto);

        ResponseEntity<Response> response = principleController.updateEmployee(employeeRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Employee successfully.", response.getBody().getMessage());
        assertEquals(employeeDto, response.getBody().getData());
    }

    @Test
    void getAllEmployee_Success() {
        List<EmployeeDto> employees = Collections.singletonList(employeeDto);
        when(principleService.getAllEmployee()).thenReturn(employees);

        ResponseEntity<Response> response = principleController.getAllEmployee();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all Employee.", response.getBody().getMessage());
        assertEquals(employees, response.getBody().getData());
    }

    @Test
    void deleteEmployee_Success() {
        doNothing().when(principleService).fireEmployee(anyLong());

        ResponseEntity<String> response = principleController.deleteEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted successfully", response.getBody());
    }

    @Test
    void updateSubject_Success() {
        when(principleService.updateSubject(any(SubjectDtoRequest.class))).thenReturn(subjectDto);

        ResponseEntity<Response> response = principleController.updateSubject(subjectRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Subject successfully.", response.getBody().getMessage());
        assertEquals(subjectDto, response.getBody().getData());
    }

    @Test
    void deleteSubject_Success() {
        doNothing().when(principleService).deleteSubject(anyLong());

        ResponseEntity<Response> response = principleController.deleteSubject(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Deleted Subject successfully.", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void restoreSubject_Success() {
        when(principleService.restoreSubject(anyLong())).thenReturn(subjectDto);

        ResponseEntity<Response> response = principleController.restoreSubject(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restored Subject successfully.", response.getBody().getMessage());
        assertEquals(subjectDto, response.getBody().getData());
    }

    @Test
    void getAllSubject_Success() {
        List<SubjectDto> subjects = Collections.singletonList(subjectDto);
        when(principleService.getAllSubject()).thenReturn(subjects);

        ResponseEntity<Response> response = principleController.getAllSubject();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all Subject.", response.getBody().getMessage());
        assertEquals(subjects, response.getBody().getData());
    }

    @Test
    void getAllActiveSubject_Success() {
        List<SubjectDto> subjects = Collections.singletonList(subjectDto);
        when(principleService.getAllActiveSubject()).thenReturn(subjects);

        ResponseEntity<Response> response = principleController.getAllActiveSubject();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully got all active Subject.", response.getBody().getMessage());
        assertEquals(subjects, response.getBody().getData());
    }

    // Error cases
    @Test
    void getScheduleById_NotFound() {
        when(principleService.getScheduleById(anyLong())).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Response> response = principleController.getScheduleById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Couldn't find"));
        assertNull(response.getBody().getData());
    }

    @Test
    void createSchedule_BadRequest() {
        when(principleService.createSchedule(any(ScheduleDtoRequest.class)))
                .thenThrow(new RuntimeException("Validation error"));

        ResponseEntity<Response> response = principleController.createSchedule(scheduleRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Schedule is not saved"));
        assertNull(response.getBody().getData());
    }

    @Test
    void deleteEmployee_NotFound() {
        doThrow(new RuntimeException("Not found")).when(principleService).fireEmployee(anyLong());

        ResponseEntity<String> response = principleController.deleteEmployee(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Failed to fire"));
    }
}