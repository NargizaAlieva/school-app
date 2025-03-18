package org.example.schoolapp.controller;

import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.service.RoleService;
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

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getRoleById() throws Exception {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setTitle("ADMIN");

        when(roleService.getDtoById(anyLong())).thenReturn(roleDto);

        mockMvc.perform(get("/ap1/v1/role/get-role-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Role with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("ADMIN"));
    }

    @Test
    void getAllRoles() throws Exception {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setTitle("ADMIN");

        when(roleService.getAllRole()).thenReturn(List.of(roleDto));

        mockMvc.perform(get("/ap1/v1/role/get-all-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Roles."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("ADMIN"));
    }

    @Test
    void getAllRoles_EmptyList() throws Exception {
        when(roleService.getAllRole()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/role/get-all-role"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No Roles found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createRole() throws Exception {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setTitle("ADMIN");

        when(roleService.createRole(any(RoleDto.class))).thenReturn(roleDto);

        mockMvc.perform(post("/ap1/v1/role/create-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"ADMIN\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Role."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("ADMIN"));
    }

    @Test
    void updateRole() throws Exception {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setTitle("UPDATED_ADMIN");

        when(roleService.updateRole(any(RoleDto.class))).thenReturn(roleDto);

        mockMvc.perform(put("/ap1/v1/role/update-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"UPDATED_ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Role with id: '1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("UPDATED_ADMIN"));
    }
}