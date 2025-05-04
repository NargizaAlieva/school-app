package org.example.schoolapp.controller;

import org.example.schoolapp.dto.*;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.service.role.AdminService;
import org.example.schoolapp.service.entity.RoleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AdminController adminController;

    private UserDtoRequest userRequest;
    private EmployeeDroRequest employeeRequest;
    private StudentDtoRequest studentRequest;
    private ParentDtoRequest parentRequest;
    private UserRoleDto userRoleRequest;
    private RoleDto roleRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserDtoRequest();
        employeeRequest = new EmployeeDroRequest();
        studentRequest = new StudentDtoRequest();
        parentRequest = new ParentDtoRequest();
        Set<Long> roles = new HashSet<>();
        roles.add(1L);
        userRoleRequest = new UserRoleDto(1L, roles);
        roleRequest = new RoleDto();
    }

    @Test
    void getAllUser_ShouldReturnUsers_WhenUsersExist() {
        List<UserDto> users = Collections.singletonList(new UserDto());
        when(adminService.getAllUser()).thenReturn(users);

        ResponseEntity<Response> response = adminController.getAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully got all Users.", response.getBody().getMessage());
        assertEquals(users, response.getBody().getData());
    }

    @Test
    void getAllUser_ShouldReturnNotFound_WhenNoUsersExist() {
        when(adminService.getAllUser()).thenThrow(new ObjectNotFoundException("No users found"));

        ResponseEntity<Response> response = adminController.getAllUser();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Couldn't find"));
    }

    @Test
    void getAllActiveUser_ShouldReturnActiveUsers() {
        List<UserDto> activeUsers = Collections.singletonList(new UserDto());
        when(adminService.getAllActiveUser()).thenReturn(activeUsers);

        ResponseEntity<Response> response = adminController.getAllActiveUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully got all active Users.", response.getBody().getMessage());
        assertEquals(activeUsers, response.getBody().getData());
    }

    @Test
    void getAllUserByRole_ShouldReturnUsersWithRole() {
        List<UserDto> users = Collections.singletonList(new UserDto());
        when(adminService.getAllUserByRole("ADMIN")).thenReturn(users);

        ResponseEntity<Response> response = adminController.getAllUserByRole("ADMIN");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("ADMIN"));
        assertEquals(users, response.getBody().getData());
    }

    @Test
    void createUser_ShouldReturnCreated_WhenSuccessful() {
        UserDto userDto = new UserDto();
        when(adminService.createUser(userRequest)).thenReturn(userDto);

        ResponseEntity<Response> response = adminController.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Successfully created User.", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenExceptionOccurs() {
        when(adminService.createUser(userRequest)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Response> response = adminController.createUser(userRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("not saved"));
    }

    @Test
    void createEmployee_ShouldReturnCreated_WhenSuccessful() {
        EmployeeDto employeeDto = new EmployeeDto();
        when(adminService.createEmployee(employeeRequest)).thenReturn(employeeDto);

        ResponseEntity<Response> response = adminController.createEmployee(employeeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Successfully created Employee.", response.getBody().getMessage());
        assertEquals(employeeDto, response.getBody().getData());
    }

    @Test
    void updateUser_ShouldReturnOk_WhenSuccessful() {
        UserDto userDto = new UserDto();
        when(adminService.updateUser(userRequest)).thenReturn(userDto);

        ResponseEntity<Response> response = adminController.updateUser(userRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated User successfully.", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenExceptionOccurs() {
        when(adminService.updateUser(userRequest)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Response> response = adminController.updateUser(userRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("not updated"));
    }

    @Test
    void deleteUser_ShouldReturnOk_WhenSuccessful() {
        doNothing().when(adminService).deleteUser(1L);

        ResponseEntity<String> response = adminController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted User successfully.", response.getBody());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserNotFound() {
        doThrow(new ObjectNotFoundException("User not found")).when(adminService).deleteUser(1L);

        ResponseEntity<String> response = adminController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Couldn't delete"));
    }

    @Test
    void addRoleToUser_ShouldReturnOk_WhenSuccessful() {
        UserDto userDto = new UserDto();
        when(adminService.addRoleToUser(userRoleRequest)).thenReturn(userDto);

        ResponseEntity<Response> response = adminController.addRoleToUser(userRoleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Added Role successfully", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
    }

    @Test
    void removeRoleFromUser_ShouldReturnOk_WhenSuccessful() {
        UserDto userDto = new UserDto();
        when(adminService.removeRoleFromUser(userRoleRequest)).thenReturn(userDto);

        ResponseEntity<Response> response = adminController.removeRoleFromUser(userRoleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Removed Role successfully", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenExists() {
        RoleDto roleDto = new RoleDto();
        when(roleService.getDtoById(1L)).thenReturn(roleDto);

        ResponseEntity<Response> response = adminController.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Successfully retrieved"));
        assertEquals(roleDto, response.getBody().getData());
    }

    @Test
    void getAllRoles_ShouldReturnRoles_WhenExist() {
        List<RoleDto> roles = Collections.singletonList(new RoleDto());
        when(roleService.getAllRole()).thenReturn(roles);

        ResponseEntity<Response> response = adminController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully retrieved all Roles.", response.getBody().getMessage());
        assertEquals(roles, response.getBody().getData());
    }

    @Test
    void createRole_ShouldReturnCreated_WhenSuccessful() {
        RoleDto roleDto = new RoleDto();
        when(roleService.createRole(roleRequest)).thenReturn(roleDto);

        ResponseEntity<Response> response = adminController.createRole(roleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Successfully created Role.", response.getBody().getMessage());
        assertEquals(roleDto, response.getBody().getData());
    }

    @Test
    void updateRole_ShouldReturnOk_WhenSuccessful() {
        RoleDto roleDto = new RoleDto();
        when(roleService.updateRole(roleRequest)).thenReturn(roleDto);

        ResponseEntity<Response> response = adminController.updateRole(roleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Successfully updated"));
        assertEquals(roleDto, response.getBody().getData());
    }
}
