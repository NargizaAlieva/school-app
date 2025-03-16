package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() throws Exception {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(1L)
                .position("Developer")
                .userDto(new UserDto())
                .build();
        when(employeeService.getDtoById(1L)).thenReturn(employeeDto);

        mockMvc.perform(get("/ap1/v1/employee/get-employee-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.position").value("Developer"));
    }

    @Test
    void getAllEmployee_ShouldReturnList() throws Exception {
        List<EmployeeDto> employees = List.of(EmployeeDto.builder()
                .id(1L)
                .position("Developer")
                .userDto(new UserDto())
                .build());
        when(employeeService.getAllEmployee()).thenReturn(employees);

        mockMvc.perform(get("/ap1/v1/employee/get-all-employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].position").value("Developer"));
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        EmployeeDroRequest request = EmployeeDroRequest.builder()
                .position("Developer")
                .userId(1L)
                .build();
        EmployeeDto response = EmployeeDto.builder()
                .id(1L)
                .position("Developer")
                .userDto(new UserDto())
                .build();;

        when(employeeService.createEmployee(any())).thenReturn(response);

        mockMvc.perform(post("/ap1/v1/employee/create-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.position").value("Developer"));
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        EmployeeDroRequest request = EmployeeDroRequest.builder()
                .position("Senior Developer")
                .userId(1L)
                .build();
        EmployeeDto response = EmployeeDto.builder()
                .id(1L)
                .position("Senior Developer")
                .userDto(new UserDto())
                .build();

        when(employeeService.updateEmployee(any())).thenReturn(response);

        mockMvc.perform(put("/ap1/v1/employee/update-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.position").value("Senior Developer"));
    }

    @Test
    void deleteEmployee_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/ap1/v1/employee/delete-employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Employee with id: 1"));
    }

    @Test
    void restoreEmployee_ShouldReturnRestoredEmployee() throws Exception {
        EmployeeDto response = EmployeeDto.builder()
                .id(1L)
                .position("Developer")
                .userDto(new UserDto())
                .build();;;;
        when(employeeService.restoreEmployee(1L)).thenReturn(response);

        mockMvc.perform(put("/ap1/v1/employee/restore-employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.position").value("Developer"));
    }
}