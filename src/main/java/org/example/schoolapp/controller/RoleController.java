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
import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.service.RoleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Role Management", description = "APIs for managing roles in the system")
@RequestMapping(value = "ap1/v1/role")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Get role by ID", description = "Retrieves a role by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/get-role-by-id/{roleId}")
    public ResponseEntity<Response> getRoleById(
            @Parameter(description = "ID of the role to retrieve", required = true)
            @PathVariable Long roleId) {
        RoleDto role = roleService.getDtoById(roleId);
        return ResponseEntity.ok(new Response("Successfully retrieved Role with Id: " + roleId, role));
    }

    @Operation(summary = "Get all roles", description = "Retrieves a list of all roles in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No roles found")
    })
    @GetMapping("/get-all-role")
    public ResponseEntity<Response> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRole();

        if (roles.isEmpty())
            throw new ObjectNotFoundException("No Roles found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Roles.", roles));
    }

    @Operation(summary = "Create a new role", description = "Creates a new role in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-role")
    public ResponseEntity<Response> createRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Role details to create", required = true,
                    content = @Content(schema = @Schema(implementation = RoleDto.class)))
            @RequestBody RoleDto request
    ) {
        RoleDto roleDto = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Role.", roleDto));
    }

    @Operation(summary = "Update a role", description = "Updates an existing role in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-role")
    public ResponseEntity<Response> updateRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Role details to update", required = true,
                    content = @Content(schema = @Schema(implementation = RoleDto.class)))
            @RequestBody RoleDto request
    ) {
        RoleDto roleDto = roleService.updateRole(request);
        return ResponseEntity.ok(new Response("Successfully updated Role with id: '" + request.getId(), roleDto));
    }
}
