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
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.service.ParentService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Parent Management", description = "APIs for managing parents in the system")
@RequestMapping(value = "ap1/v1/parent")
public class ParentController {
    private final ParentService parentService;

    @Operation(summary = "Get parent by ID", description = "Retrieves a parent by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parent found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Parent not found")
    })
    @GetMapping("/get-parent-by-id/{parentId}")
    public ResponseEntity<Response> getParentById(
            @Parameter(description = "ID of the parent to retrieve", required = true)
            @PathVariable Long parentId) {
        ParentDto parentDto = parentService.getDtoById(parentId);
        return ResponseEntity.ok(new Response("Successfully retrieved Parent with Id: " + parentId, parentDto));
    }

    @Operation(summary = "Get all parents", description = "Retrieves a list of all parents in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parents found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No parents found")
    })
    @GetMapping("/get-all-parent")
    public ResponseEntity<Response> getAllParent() {
        List<ParentDto> parents = parentService.getAllParent();

        if (parents.isEmpty())
            throw new ObjectNotFoundException("No Parent found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Parents.", parents));
    }

    @Operation(summary = "Get all active parents", description = "Retrieves a list of all active parents in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active parents found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active parents found")
    })
    @GetMapping("/get-all-active-parent")
    public ResponseEntity<Response> getAllActiveParent() {
        List<ParentDto> parents = parentService.getAllActiveParent();

        if (parents.isEmpty())
            throw new ObjectNotFoundException("No active Parent found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Parents.", parents));
    }

    @Operation(summary = "Create a new parent", description = "Creates a new parent in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parent created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-parent")
    public ResponseEntity<Response> createParent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Parent details to create", required = true,
                    content = @Content(schema = @Schema(implementation = ParentDtoRequest.class)))
            @RequestBody ParentDtoRequest request
    ) {
        ParentDto parentDto = parentService.createParent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Parent.", parentDto));
    }

    @Operation(summary = "Update a parent", description = "Updates an existing parent in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parent updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-parent")
    public ResponseEntity<Response> updateParent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Parent details to update", required = true,
                    content = @Content(schema = @Schema(implementation = ParentDtoRequest.class)))
            @RequestBody ParentDtoRequest request
    ) {
        ParentDto parentDto = parentService.updateParent(request);
        return ResponseEntity.ok(new Response("Successfully updated Parent with id: " + request.getId(), parentDto));
    }

    @Operation(summary = "Delete a parent", description = "Deletes a parent from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parent deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-parent/{parentId}")
    public ResponseEntity<Response> deleteParent(
            @Parameter(description = "ID of the parent to delete", required = true)
            @PathVariable Long parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.ok(new Response("Successfully deleted Parent with id: " + parentId, null));
    }

    @Operation(summary = "Restore a parent", description = "Restores a previously deleted parent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parent restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-parent/{parentId}")
    public ResponseEntity<Response> restoreParent(
            @Parameter(description = "ID of the parent to restore", required = true)
            @PathVariable Long parentId) {
        ParentDto parentDto = parentService.restoreParent(parentId);
        return ResponseEntity.ok(new Response("Successfully restored Parent with id: " + parentId, parentDto));
    }
}
