package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.service.EmployeeService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getEmployeeById() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.getDtoById(anyLong())).thenReturn(employeeDto);

        mockMvc.perform(get("/ap1/v1/employee/get-employee-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Employee with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.position").value("Teacher"))
                .andExpect(jsonPath("$.data.salary").value(50000));
    }

    @Test
    void getAllEmployees() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.getAllEmployee()).thenReturn(List.of(employeeDto));

        mockMvc.perform(get("/ap1/v1/employee/get-all-employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Employees."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].position").value("Teacher"))
                .andExpect(jsonPath("$.data[0].salary").value(50000));
    }

    @Test
    void getAllEmployees_EmptyList() throws Exception {
        when(employeeService.getAllEmployee()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/employee/get-all-employee"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Employees found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getAllActiveEmployees() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.getAllActiveEmployee()).thenReturn(List.of(employeeDto));

        mockMvc.perform(get("/ap1/v1/employee/get-all-active-employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Employees."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].position").value("Teacher"))
                .andExpect(jsonPath("$.data[0].salary").value(50000));
    }

    @Test
    void getAllActiveEmployees_EmptyList() throws Exception {
        when(employeeService.getAllActiveEmployee()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/employee/get-all-active-employee"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No active Employees found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getEmployeeBySubjectId() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.getBySubjectId(anyLong())).thenReturn(List.of(employeeDto));

        mockMvc.perform(get("/ap1/v1/employee/get-teacher-by-subject-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Employees with subjectId.1"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].position").value("Teacher"))
                .andExpect(jsonPath("$.data[0].salary").value(50000));
    }

    @Test
    void getEmployeeBySubjectId_EmptyList() throws Exception {
        when(employeeService.getBySubjectId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/employee/get-teacher-by-subject-id/1"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Employees found with subjectId: 1."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getEmployeeByGradeId() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.getHomeTeacherByGradeId(anyLong())).thenReturn(employeeDto);

        mockMvc.perform(get("/ap1/v1/employee/get-home-teacher-by-grade-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved home teacher of grade with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.position").value("Teacher"))
                .andExpect(jsonPath("$.data.salary").value(50000));
    }

    @Test
    void createEmployee() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.createEmployee(any(EmployeeDroRequest.class))).thenReturn(employeeDto);

        mockMvc.perform(post("/ap1/v1/employee/create-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": \"Teacher\", \"salary\": 50000, \"userId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Employee."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.position").value("Teacher"))
                .andExpect(jsonPath("$.data.salary").value(50000));
    }

    @Test
    void updateEmployee() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Updated Teacher");
        employeeDto.setSalary(60000);

        when(employeeService.updateEmployee(any(EmployeeDroRequest.class))).thenReturn(employeeDto);

        mockMvc.perform(put("/ap1/v1/employee/update-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"position\": \"Updated Teacher\", \"salary\": 60000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Employee with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.position").value("Updated Teacher"))
                .andExpect(jsonPath("$.data.salary").value(60000));
    }

    @Test
    void deleteEmployee() throws Exception {
        mockMvc.perform(delete("/ap1/v1/employee/delete-employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Employee with id: 1"));
    }

    @Test
    void restoreEmployee() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setPosition("Restored Teacher");
        employeeDto.setSalary(50000);

        when(employeeService.restoreEmployee(anyLong())).thenReturn(employeeDto);

        mockMvc.perform(put("/ap1/v1/employee/restore-employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Employee with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.position").value("Restored Teacher"))
                .andExpect(jsonPath("$.data.salary").value(50000));
    }
}