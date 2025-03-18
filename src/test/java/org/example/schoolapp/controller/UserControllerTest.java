package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.UserRoleDto;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void getUserById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");

        when(userService.getById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/ap1/v1/user/get-user-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved User with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void getUserByUsername() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");

        when(userService.getByUsername(anyString())).thenReturn(userDto);

        mockMvc.perform(get("/ap1/v1/user/get-user-by-username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved User with username: testuser"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void getUserByEmail() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        when(userService.getByEmail(anyString())).thenReturn(userDto);

        mockMvc.perform(get("/ap1/v1/user/get-user-by-email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved User with email: test@example.com"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void getAllUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");

        when(userService.getAllUser()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/ap1/v1/user/get-all-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Users."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("testuser"));
    }

    @Test
    void getAllActiveUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setIsActive(true);

        when(userService.getAllActiveUser()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/ap1/v1/user/get-all-active-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Users."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("testuser"))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllUsersByRole() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setRoleSet(Collections.singleton("ADMIN"));

        when(userService.getUserDtoListWithRole(anyString())).thenReturn(List.of(userDto));

        mockMvc.perform(get("/ap1/v1/user/get-all-user-by-role/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Users with Role:ADMIN"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("testuser"))
                .andExpect(jsonPath("$.data[0].roleSet[0]").value("ADMIN"));
    }

    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");

        when(userService.createUser(any(UserDtoRequest.class))).thenReturn(userDto);

        mockMvc.perform(post("/ap1/v1/user/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created User."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("updateduser");

        when(userService.updateUser(any(UserDtoRequest.class))).thenReturn(userDto);

        mockMvc.perform(put("/ap1/v1/user/update-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"username\": \"updateduser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated User with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("updateduser"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/ap1/v1/user/delete-user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted User with id: 1"));
    }

    @Test
    void restoreUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("restoreduser");

        when(userService.restoreUser(anyLong())).thenReturn(userDto);

        mockMvc.perform(put("/ap1/v1/user/restore-user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored User with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("restoreduser"));
    }

    @Test
    void addRoleToUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setRoleSet(new HashSet<>(Collections.singleton("ADMIN")));

        when(userService.addRoleToUser(any(UserRoleDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/ap1/v1/user/add-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"roleIdSet\": [1]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully added Role with ids: '[1]' to user with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.roleSet[0]").value("ADMIN"));
    }

    @Test
    void removeRoleFromUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setRoleSet(new HashSet<>());

        when(userService.removeRoleFromUser(any(UserRoleDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/ap1/v1/user/remove-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"roleIdSet\": [1]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully removed Role with ids: '[1]' to user with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.roleSet").isEmpty());
    }
}