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

import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.service.role.ClassTeacherService;

@RestController
@RequestMapping(value = "/api/v1/class-teacher")
@AllArgsConstructor
@Tag(name = "Class Teacher Management", description = "Operations related to class teacher role.")
public class ClassTeacherController {
    private final ClassTeacherService classTeacherService;

    @Operation(
            summary = "Get all students from class",
            description = "Retrieves all students from the class."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all class students", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/get-all-class-student")
    public ResponseEntity<Response> studentsFromClass() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all class Student from home grades.", classTeacherService.getAllStudentsFromClass()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @GetMapping("/get-all-home-grade")
    public ResponseEntity<Response> getAllHomeGrades() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all home grades.", classTeacherService.getTeacherGradesList()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Create a new mark",
            description = "Creates a new mark based on the provided mark data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created Mark", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mark data", content = @Content)
    })
    @PostMapping(value = "/create-mark")
    public ResponseEntity<Response> createMark(@RequestBody MarkDtoRequest request) {
        try {
            MarkDto markDto = classTeacherService.createMark(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Mark.", markDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Mark is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update an existing mark",
            description = "Updates an existing mark based on the provided mark data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Mark", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mark data", content = @Content)
    })
    @PutMapping(value = "/update-mark")
    public ResponseEntity<Response> updateMark(@RequestBody MarkDtoRequest request) {
        try {
            MarkDto markDto = classTeacherService.updateMark(request);
            return ResponseEntity.ok(new Response("Successfully updated Mark.", markDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Mark is not saved. " + exception.getMessage(), null));
        }
    }
}
