package org.example.schoolapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.UserService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "User Management", description = "APIs for managing users in the system")
@RequestMapping(value = "ap1/v1/user")
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/get-user-by-id/{userId}")
    public ResponseEntity<Response> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long userId) {
        UserDto user = userService.getById(userId);
        return ResponseEntity.ok(new Response("Successfully retrieved User with Id: " + userId, user));
    }

    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/get-user-by-username/{username}")
    public ResponseEntity<Response> getUserByUsername(
            @Parameter(description = "Username of the user to retrieve", required = true)
            @PathVariable String username) {
        UserDto users = userService.getByUsername(username);
        return ResponseEntity.ok(new Response("Successfully retrieved User with username: " + username, users));
    }

    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/get-user-by-email/{email}")
    public ResponseEntity<Response> getUserByEmail(
            @Parameter(description = "Email address of the user to retrieve", required = true)
            @PathVariable String email) {
        UserDto users = userService.getByEmail(email);

        return ResponseEntity.ok(new Response("Successfully retrieved User with email: " + email, users));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No users found")
    })
    @GetMapping("/get-all-user")
    public ResponseEntity<Response> getAllUsers() {
        List<UserDto> users = userService.getAllUser();

        if (users.isEmpty())
            throw new ObjectNotFoundException("No Users found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Users.", users));
    }

    @Operation(summary = "Get all active users", description = "Retrieves a list of all active users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active users found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active users found")
    })
    @GetMapping("/get-all-active-user")
    public ResponseEntity<Response> getAllActiveUser() {
        List<UserDto> users = userService.getAllActiveUser();

        if (users.isEmpty())
            throw new ObjectNotFoundException("No active Users found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Users.", users));
    }

    @Operation(summary = "Get users by role", description = "Retrieves a list of users by their role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No users found with the specified role")
    })
    @GetMapping("/get-all-user-by-role/{role}")
    public ResponseEntity<Response> getAllUserByRole(
            @Parameter(description = "Role of the users to retrieve", required = true)
            @PathVariable String role) {
        List<UserDto> users = userService.getUserDtoListWithRole(role);

        if (users.isEmpty())
            throw new ObjectNotFoundException("No Users with role: " + role + " found.");

        return ResponseEntity.ok(new Response("Successfully got all Users with Role:" + role, users));
    }

    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-user")
    public ResponseEntity<Response> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User details to create", required = true,
                content = @Content(schema = @Schema(implementation = UserDtoRequest.class)))
            @RequestBody UserDtoRequest request) {
        UserDto userDto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created User.", userDto));
    }

    @Operation(summary = "Update a user", description = "Updates an existing user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-user")
    public ResponseEntity<Response> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User details to update", required = true,
                content = @Content(schema = @Schema(implementation = UserDtoRequest.class)))
            @RequestBody UserDtoRequest request) {
        UserDto updatedUser = userService.updateUser(request);
        return ResponseEntity.ok(new Response("Successfully updated User with id: " + request.getId(), updatedUser));
    }

    @Operation(summary = "Delete a user", description = "Deletes a user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-user/{userId}")
    public ResponseEntity<Response> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new Response("Successfully deleted User with id: " + userId, null));
    }

    @Operation(summary = "Restore a user", description = "Restores a previously deleted user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-user/{userId}")
    public ResponseEntity<Response> restoreUser(
            @Parameter(description = "ID of the user to restore", required = true)
            @PathVariable Long userId) {
        UserDto userDto = userService.restoreUser(userId);
        return ResponseEntity.ok(new Response("Successfully restored User with id: " + userId, userDto));
    }

    @Operation(summary = "Add roles to a user", description = "Adds one or more roles to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles added",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/add-role")
    public ResponseEntity<Response> addRoleToUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User and role details", required = true,
                content = @Content(schema = @Schema(implementation = UserRoleDto.class)))
            @RequestBody UserRoleDto request) {
        UserDto updatedUser = userService.addRoleToUser(request);
        return ResponseEntity.ok(new Response("Successfully added Role with ids: '" + request.getRoleIdSet()
                + "' to user with id: " + request.getUserId(), updatedUser));
    }

    @Operation(summary = "Remove roles from a user", description = "Removes one or more roles from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles removed",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/remove-role")
    public ResponseEntity<Response> removeRoleFromUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User and role details", required = true,
                content = @Content(schema = @Schema(implementation = UserRoleDto.class)))
            @RequestBody UserRoleDto request) {
        UserDto updatedUser = userService.removeRoleFromUser(request);
        return ResponseEntity.ok(new Response("Successfully removed Role with ids: '" + request.getRoleIdSet()
                + "' to user with id: " + request.getUserId(), updatedUser));
    }
}
