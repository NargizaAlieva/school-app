package org.example.schoolapp.integration;

import org.example.schoolapp.dto.response.RoleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetRoleById() throws Exception {
        mockMvc.perform(get("/ap1/v1/role/get-role-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Role with Id: 1"));
    }

    @Test
    void testGetAllRoles() throws Exception {
        mockMvc.perform(get("/ap1/v1/role/get-all-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Roles."));
    }

    @Test
    void testCreateRole() throws Exception {
        RoleDto newRole = RoleDto.builder()
                .title("Principle")
                .build();

        mockMvc.perform(post("/ap1/v1/role/create-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Role."));
    }

    @Test
    void testUpdateRole() throws Exception {
        RoleDto updatedRole = RoleDto.builder()
                .id(1L)
                .title("Super Admin")
                .build();

        mockMvc.perform(put("/ap1/v1/role/update-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Role with id: '1"));
    }
}
