package org.example.schoolapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.example.schoolapp.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.service.role.ParentRoleService;

@RestController
@RequestMapping(value = "/api/v1/parent")
@AllArgsConstructor
@Tag(name = "Parent Management", description = "Operations related to parent role.")
public class ParentController {
    private final ParentRoleService parentRoleService;

    @Operation(summary = "Create a new student",
            description = "Creates a new student based on the provided student details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created Student.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Student is not saved due to validation errors.", content = @Content)
    })
    @PostMapping(value = "/create-student")
    public ResponseEntity<Response> createStudent(@RequestBody StudentDtoRequest request) {
        try {
            StudentDto studentDto = parentRoleService.createStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Student.", studentDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Student is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Get all children",
            description = "Retrieves a list of all children associated with the parent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got all children.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Children not found.", content = @Content)
    })
    @GetMapping("/get-all-child")
    public ResponseEntity<Response> getChildList() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all children.", parentRoleService.getChildList()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Children not found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get student's schedule",
            description = "Retrieves the schedule for a specific child. " +
                    "This endpoint allows parents to check their child's daily or weekly schedule."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved student's schedule.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found for the specified child.",
                    content = @Content)
    })
    @GetMapping("/get-student-schedule/{childId}")
    public ResponseEntity<Response> getStudentSchedule(@PathVariable Long childId) {
        try {
            return ResponseEntity.ok(new Response("Successfully got Schedules.", parentRoleService.getStudentSchedule(childId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Delete a student by ID",
            description = "Deletes a specific student from the school by their ID. " +
                    "This endpoint is used to remove a student from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the student.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found for the specified ID.",
                    content = @Content)
    })
    @DeleteMapping(value = "/delete-user/{childId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long childId) {
        try {
            parentRoleService.leaveSchool(childId);
            return ResponseEntity.ok("Deleted Student successfully.");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete. " + exception.getMessage());
        }
    }
}
