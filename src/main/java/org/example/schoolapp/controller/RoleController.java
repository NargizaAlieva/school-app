package org.example.schoolapp.controller;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.service.RoleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "ap1/v1/role")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/get-role-by-id/{roleId}")
    public ResponseEntity<Response> getRoleById(@PathVariable Long roleId) {
        RoleDto role = roleService.getDtoById(roleId);
        return ResponseEntity.ok(new Response("Successfully retrieved Role with Id: " + roleId, role));
    }

    @GetMapping("/get-all-role")
    public ResponseEntity<Response> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRole();

        if (roles.isEmpty())
            throw new ObjectNotFoundException("No Roles found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Roles.", roles));
    }

    @PostMapping(value = "/create-role")
    public ResponseEntity<Response> createRole(@RequestBody RoleDto request) {
        RoleDto roleDto = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Role.", roleDto));
    }

    @PutMapping(value = "/update-role")
    public ResponseEntity<Response> updateRole(@RequestBody RoleDto request) {
        RoleDto roleDto = roleService.updateRole(request);
        return ResponseEntity.ok(new Response("Successfully updated Role with id: '" + request.getId(), roleDto));
    }
}
