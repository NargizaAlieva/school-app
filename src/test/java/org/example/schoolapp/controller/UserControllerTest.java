package org.example.schoolapp.controller;

import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private UserDto buildUserDto(Long id, String username, String email) {
        return UserDto.builder()
                .id(id)
                .username(username)
                .email(email)
                .isActive(true)
                .build();
    }

    private UserDtoRequest buildUserDtoRequest(Long id, String username, String email) {
        return UserDtoRequest.builder()
                .id(id)
                .username(username)
                .email(email)
                .password("password123")
                .build();
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        UserDto userDto = buildUserDto(1L, "testuser", "test@example.com");
        when(userService.getById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/ap1/v1/user/get-user-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved User with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserDtoRequest request = buildUserDtoRequest(null, "newuser", "newuser@example.com");
        UserDto userDto = buildUserDto(2L, "newuser", "newuser@example.com");

        when(userService.createUser(any(UserDtoRequest.class))).thenReturn(userDto);

        mockMvc.perform(post("/ap1/v1/user/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username \":\"newuser\",\"email\":\"newuser@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created User."))
                .andExpect(jsonPath("$.data.username").value("newuser"));
    }

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        UserDto userDto = buildUserDto(1L, "testuser", "test@example.com");
        when(userService.getAllUser()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/ap1/v1/user/get-all-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Users."))
                .andExpect(jsonPath("$.data[0].username").value("testuser"));
    }

    @Test
    void deleteUser_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/ap1/v1/user/delete-user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted User with id: 1"));
    }

    @Test
    void addRoleToUser_shouldReturnUpdatedUser() throws Exception {
        UserRoleDto request = new UserRoleDto(1L, new HashSet<>(Collections.singletonList(2L)));
        UserDto userDto = buildUserDto(1L, "testuser", "test@example.com");

        when(userService.addRoleToUser(any(UserRoleDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/ap1/v1/user/add-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"roleIdSet\":[2]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully added Role with ids: '[2]' to user with id: 1"))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }
}