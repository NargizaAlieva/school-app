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
import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Grade Management", description = "APIs for managing grades in the system")
@RequestMapping(value = "ap1/v1/grade")
public class GradeController {
    private final GradeService gradeService;

    @Operation(summary = "Get grade by ID", description = "Retrieves a grade by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @GetMapping("/get-grade-by-id/{gradeId}")
    public ResponseEntity<Response> getGradeById(
            @Parameter(description = "ID of the grade to retrieve", required = true)
            @PathVariable Long gradeId) {
        GradeDto gradeDto = gradeService.getById(gradeId);
        return ResponseEntity.ok(new Response("Successfully retrieved Grade with Id: " + gradeId, gradeDto));
    }

    @Operation(summary = "Get all grades", description = "Retrieves a list of all grades in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grades found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No grades found")
    })
    @GetMapping("/get-all-grade")
    public ResponseEntity<Response> getAllGrade() {
        List<GradeDto> grades = gradeService.getAllGrade();

        if (grades.isEmpty())
            throw new ObjectNotFoundException("No Grades found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Grades.", grades));
    }

    @Operation(summary = "Get all active grades", description = "Retrieves a list of all active grades in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active grades found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active grades found")
    })
    @GetMapping("/get-all-active-grade")
    public ResponseEntity<Response> getAllActiveGrade() {
        List<GradeDto> grades = gradeService.getAllActiveGrade();

        if (grades.isEmpty())
            throw new ObjectNotFoundException("No active Grades found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Grades.", grades));
    }

    @Operation(summary = "Get grades by teacher ID", description = "Retrieves a list of grades for a specific teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grades found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No grades found for the given teacher ID")
    })
    @GetMapping("/get-grade-by-teacherId/{teacherId}")
    public ResponseEntity<Response> getGradeByTeacherId(
            @Parameter(description = "ID of the teacher to retrieve grades for", required = true)
            @PathVariable Long teacherId) {
        List<GradeDto> grades = gradeService.getAllGradeByTeacherId(teacherId);

        if (grades.isEmpty())
            throw new ObjectNotFoundException("No Grades with home teacher with id: " + teacherId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Grades with home teacher with id: " + teacherId, grades));
    }

    @Operation(summary = "Get grade by student ID", description = "Retrieves the grade for a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No grade found for the given student ID")
    })
    @GetMapping("/get-grade-by-studentId/{studentId}")
    public ResponseEntity<Response> getGradeByStudentId(
            @Parameter(description = "ID of the student to retrieve grade for", required = true)
            @PathVariable Long studentId) {
        GradeDto gradeDto = gradeService.getGradeByStudentId(studentId);
        return ResponseEntity.ok(new Response("Successfully retrieved Grade with student by id: " + studentId, gradeDto));
    }

    @Operation(summary = "Create a new grade", description = "Creates a new grade in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grade created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-grade")
    public ResponseEntity<Response> createGrade(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Grade details to create", required = true,
                    content = @Content(schema = @Schema(implementation = GradeDtoRequest.class)))
            @RequestBody GradeDtoRequest request
    ) {
        GradeDto gradeDto = gradeService.createGrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Grade.", gradeDto));
    }

    @Operation(summary = "Update a grade", description = "Updates an existing grade in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-grade")
    public ResponseEntity<Response> updateGrade(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Grade details to update", required = true,
                    content = @Content(schema = @Schema(implementation = GradeDtoRequest.class)))
            @RequestBody GradeDtoRequest request
    ) {
        GradeDto gradeDto = gradeService.updateGrade(request);
        return ResponseEntity.ok(new Response("Successfully updated Grade with id: " + request.getId(), gradeDto));
    }

    @Operation(summary = "Delete a grade", description = "Deletes a grade from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-grade/{gradeId}")
    public ResponseEntity<Response> deleteGrade(
            @Parameter(description = "ID of the grade to delete", required = true)
            @PathVariable Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return ResponseEntity.ok(new Response("Successfully deleted Grade with id: " + gradeId, null));
    }

    @Operation(summary = "Restore a grade", description = "Restores a previously deleted grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-grade/{gradeId}")
    public ResponseEntity<Response> restoreGrade(
            @Parameter(description = "ID of the grade to restore", required = true)
            @PathVariable Long gradeId) {
        GradeDto gradeDto = gradeService.restoreGrade(gradeId);
        return ResponseEntity.ok(new Response("Successfully restored Grade with id: " + gradeId, gradeDto));
    }
}
