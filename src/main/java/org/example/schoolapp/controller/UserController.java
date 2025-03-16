package org.example.schoolapp.controller;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.UserService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "ap1/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/get-user-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable Long userId) {
        UserDto user = userService.getById(userId);
        return ResponseEntity.ok(new Response("Successfully retrieved User with Id: " + userId, user));
    }

    @GetMapping("/get-user-by-username/{username}")
    public ResponseEntity<Response> getUserByUsername(@PathVariable String username) {
        UserDto users = userService.getByUsername(username);
        return ResponseEntity.ok(new Response("Successfully retrieved User with username: " + username, users));
    }

    @GetMapping("/get-user-by-email/{email}")
    public ResponseEntity<Response> getUserByEmail(@PathVariable String email) {
        UserDto users = userService.getByEmail(email);

        return ResponseEntity.ok(new Response("Successfully retrieved User with email: " + email, users));
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<Response> getAllUsers() {
        List<UserDto> users = userService.getAllUser();

        if (users.isEmpty())
            throw new ObjectNotFoundException("No Users found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Users.", users));
    }

    @GetMapping("/get-all-active-user")
    public ResponseEntity<Response> getAllActiveUser() {
        List<UserDto> users = userService.getAllActiveUser();

        if (users.isEmpty())
            throw new ObjectNotFoundException("No active Users found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Users.", users));
    }

    @GetMapping("/get-all-user-by-role/{role}")
    public ResponseEntity<Response> getAllUserByRole(@PathVariable String role) {
        List<UserDto> users = userService.getUserDtoListWithRole(role);

        if (users.isEmpty())
            throw new ObjectNotFoundException("No Users with role: " + role + " found.");

        return ResponseEntity.ok(new Response("Successfully got all Users with Role:" + role, users));
    }

    @PostMapping(value = "/create-user")
    public ResponseEntity<Response> createUser(@RequestBody UserDtoRequest request) {
        UserDto userDto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created User.", userDto));
    }

    @PutMapping(value = "/update-user")
    public ResponseEntity<Response> updateUser(@RequestBody UserDtoRequest request) {
        UserDto updatedUser = userService.updateUser(request);
        return ResponseEntity.ok(new Response("Successfully updated User with id: " + request.getId(), updatedUser));
    }

    @DeleteMapping(value = "/delete-user/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new Response("Successfully deleted User with id: " + userId, null));
    }

    @PutMapping(value = "/restore-user/{userId}")
    public ResponseEntity<Response> restoreUser(@PathVariable Long userId) {
        UserDto userDto = userService.restoreUser(userId);
        return ResponseEntity.ok(new Response("Successfully restored User with id: " + userId, userDto));
    }

    @PutMapping(value = "/add-role")
    public ResponseEntity<Response> addRoleToUser(@RequestBody UserRoleDto request) {
        UserDto updatedUser = userService.addRoleToUser(request);
        return ResponseEntity.ok(new Response("Successfully added Role with ids: '" + request.getRoleIdSet()
                + "' to user with id: " + request.getUserId(), updatedUser));
    }

    @PutMapping(value = "/remove-role")
    public ResponseEntity<Response> removeRoleFromUser(@RequestBody UserRoleDto request) {
        UserDto updatedUser = userService.removeRoleFromUser(request);
        return ResponseEntity.ok(new Response("Successfully removed Role with ids: '" + request.getRoleIdSet()
                + "' to user with id: " + request.getUserId(), updatedUser));
    }
}
