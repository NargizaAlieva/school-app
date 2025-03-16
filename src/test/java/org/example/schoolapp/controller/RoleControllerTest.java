package org.example.schoolapp.controller;

import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.service.RoleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    private RoleDto createRoleDto(Long id, String name) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(id);
        roleDto.setTitle(name);
        return roleDto;
    }

    @Test
    void getRoleById_ShouldReturnRole() throws Exception {
        RoleDto roleDto = createRoleDto(1L, "Admin");
        when(roleService.getDtoById(1L)).thenReturn(roleDto);

        mockMvc.perform(get("/ap1/v1/role/get-role-by-id/{roleId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Role with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Admin"));
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() throws Exception {
        List<RoleDto> roles = List.of(createRoleDto(1L, "Admin"), createRoleDto(2L, "User"));
        when(roleService.getAllRole()).thenReturn(roles);

        mockMvc.perform(get("/ap1/v1/role/get-all-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Roles."))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    void getAllRoles_ShouldReturnNotFound_WhenEmpty() throws Exception {
        when(roleService.getAllRole()).thenThrow(new ObjectNotFoundException("No Roles found."));

        mockMvc.perform(get("/ap1/v1/role/get-all-role"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No Roles found."));
    }

    @Test
    void createRole_ShouldReturnCreatedRole() throws Exception {
        RoleDto request = createRoleDto(null, "Moderator");
        RoleDto response = createRoleDto(4L, "Moderator");
        when(roleService.createRole(any(RoleDto.class))).thenReturn(response);

        mockMvc.perform(post("/ap1/v1/role/create-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Moderator\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Role."))
                .andExpect(jsonPath("$.data.id").value(4L))
                .andExpect(jsonPath("$.data.title").value("Moderator"));
    }

    @Test
    void updateRole_ShouldReturnUpdatedRole() throws Exception {
        RoleDto request = createRoleDto(1L, "Super Admin");
        when(roleService.updateRole(any(RoleDto.class))).thenReturn(request);

        mockMvc.perform(put("/ap1/v1/role/update-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Super Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Role with id: '1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Super Admin"));
    }
}