package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.EmployeeController;
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.service.EmployeeService;
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
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto employeeDto;
    private EmployeeDroRequest employeeDroRequest;

    @BeforeEach
    void setUp() {
        employeeDto = EmployeeDto.builder()
                .id(1L)
                .position("Teacher")
                .salary(50000)
                .subjectSet(Set.of("Math", "Science"))
                .build();

        employeeDroRequest = EmployeeDroRequest.builder()
                .id(1L)
                .position("Teacher")
                .salary(50000)
                .userId(1L)
                .subjectSet(Set.of(1L, 2L))
                .build();
    }

    @Test
    void testGetEmployeeById() throws Exception {
        Mockito.when(employeeService.getDtoById(1L)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/employee/get-employee-by-id/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.position").value("Teacher"));
    }

    @Test
    void testGetAllEmployee() throws Exception {
        Mockito.when(employeeService.getAllEmployee()).thenReturn(Collections.singletonList(employeeDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/employee/get-all-employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].position").value("Teacher"));
    }

    @Test
    void testGetAllActiveEmployee() throws Exception {
        Mockito.when(employeeService.getAllActiveEmployee()).thenReturn(Collections.singletonList(employeeDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/employee/get-all-active-employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].position").value("Teacher"));
    }

    @Test
    void testGetEmployeeBySubjectId() throws Exception {
        Mockito.when(employeeService.getBySubjectId(1L)).thenReturn(Collections.singletonList(employeeDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/employee/get-teacher-by-subject-id/{subjectId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].position").value("Teacher"));
    }

    @Test
    void testGetEmployeeByGradeId() throws Exception {
        Mockito.when(employeeService.getHomeTeacherByGradeId(1L)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/employee/get-home-teacher-by-grade-id/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.position").value("Teacher"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        Mockito.when(employeeService.createEmployee(employeeDroRequest)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/employee/create-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDroRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.position").value("Teacher"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Mockito.when(employeeService.updateEmployee(employeeDroRequest)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/employee/update-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDroRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.position").value("Teacher"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/employee/delete-employee/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted Employee with id: 1"));
    }

    @Test
    void testRestoreEmployee() throws Exception {
        Mockito.when(employeeService.restoreEmployee(1L)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/employee/restore-employee/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.position").value("Teacher"));
    }
}