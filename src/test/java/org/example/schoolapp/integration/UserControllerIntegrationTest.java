package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetUserById() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setUsername("testUser");

        Mockito.when(userService.getById(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/user/get-user-by-id/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved User with Id: " + userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("testUser"))
                .andDo(print());
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        String username = "testUser";
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername(username);

        Mockito.when(userService.getByUsername(username)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/user/get-user-by-username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved User with username: " + username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(username))
                .andDo(print());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        String email = "test@example.com";
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail(email);

        Mockito.when(userService.getByEmail(email)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/user/get-user-by-email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved User with email: " + email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(email))
                .andDo(print());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testUser");

        Mockito.when(userService.getAllUser()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/user/get-all-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Users."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value("testUser"))
                .andDo(print());
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDtoRequest request = new UserDtoRequest();
        request.setUsername("newUser");
        request.setEmail("new@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername(request.getUsername());
        userDto.setEmail(request.getEmail());

        Mockito.when(userService.createUser(request)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/user/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created User."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("newUser"))
                .andDo(print());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDtoRequest request = new UserDtoRequest();
        request.setId(1L);
        request.setUsername("updatedUser");
        request.setEmail("updated@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(request.getId());
        userDto.setUsername(request.getUsername());
        userDto.setEmail(request.getEmail());

        Mockito.when(userService.updateUser(request)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/user/update-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated User with id: " + request.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("updatedUser"))
                .andDo(print());
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/user/delete-user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted User with id: " + userId))
                .andDo(print());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }

    @Test
    public void testRestoreUser() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setUsername("restoredUser");

        Mockito.when(userService.restoreUser(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/user/restore-user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully restored User with id: " + userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("restoredUser"))
                .andDo(print());
    }

    @Test
    public void testAddRoleToUser() throws Exception {
        UserRoleDto request = new UserRoleDto();
        request.setUserId(1L);
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(1L);
        request.setRoleIdSet(roleIds);

        UserDto userDto = new UserDto();
        userDto.setId(request.getUserId());
        userDto.setUsername("userWithRole");

        Mockito.when(userService.addRoleToUser(request)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/user/add-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully added Role with ids: '[1]' to user with id: " + request.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("userWithRole"))
                .andDo(print());
    }

    @Test
    public void testRemoveRoleFromUser() throws Exception {
        UserRoleDto request = new UserRoleDto();
        request.setUserId(1L);
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(1L);
        request.setRoleIdSet(roleIds);

        UserDto userDto = new UserDto();
        userDto.setId(request.getUserId());
        userDto.setUsername("userWithoutRole");

        Mockito.when(userService.removeRoleFromUser(request)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/user/remove-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully removed Role with ids: '[1]' to user with id: " + request.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("userWithoutRole"))
                .andDo(print());
    }
}