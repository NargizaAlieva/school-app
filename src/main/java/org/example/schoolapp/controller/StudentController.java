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

import org.example.schoolapp.service.role.StudentRoleService;

@RestController
@RequestMapping(value = "/api/v1/student")
@AllArgsConstructor
@Tag(name = "Student Management", description = "Operations related to student role.")
public class StudentController {
    private final StudentRoleService studentRoleService;

    @Operation(
            summary = "Get all students' marks",
            description = "Retrieves all students' marks."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got all Students Marks", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Marks not found", content = @Content)
    })
    @GetMapping("/get-all-mark")
    public ResponseEntity<Response> getAllMark() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all Students Marks.", studentRoleService.getAllMark()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Marks not found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all marks by subject",
            description = "Retrieves all marks for the specified subject."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got Marks", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Marks not found", content = @Content)
    })
    @GetMapping("/get-all-mark-subject/{subjectId}")
    public ResponseEntity<Response> getAllMarkBySubjectQuarter(@PathVariable Long subjectId) {
        try {
            return ResponseEntity.ok(new Response("Successfully got Marks. ", studentRoleService.getAvgMarkBySubjectGradeStudent(subjectId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Marks not found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get average marks for the year",
            description = "Retrieves average marks for all students for the year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got average marks", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Marks not found", content = @Content)
    })
    @GetMapping("/get-all-mark-for-year")
    public ResponseEntity<Response> getAvgMarkByGradeStudent() {
        try {
            return ResponseEntity.ok(new Response("Successfully got Marks ", studentRoleService.getAvgMarkByGradeStudent()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Marks not found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get student schedule",
            description = "Retrieves the schedule for a specific student."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got Schedules", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedules not found", content = @Content)
    })
    @GetMapping("/get-student-schedule")
    public ResponseEntity<Response> getStudentSchedule() {
        try {
            return ResponseEntity.ok(new Response("Successfully got Schedules.", studentRoleService.getStudentSchedule()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Schedules is not found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all lessons by grade",
            description = "Retrieves all lessons assigned to a specific grade."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got Lessons", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Lessons topic not found", content = @Content)
    })
    @GetMapping("/get-all-lesson-grade")
    public ResponseEntity<Response> getAllLessonByGrade() {
        try {
            return ResponseEntity.ok(new Response("Successfully got Lessons.", studentRoleService.getAllLessonByGrade()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Lessons topic is not found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get classmates",
            description = "Retrieves a list of classmates for a specific student."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got classmates", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Classmates not found", content = @Content)
    })
    @GetMapping("/get-classmates")
    public ResponseEntity<Response> getClassmates() {
        try {
            return ResponseEntity.ok(new Response("Successfully got Classmates.", studentRoleService.getClassmates()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Classmates is not found. " + exception.getMessage(), null));
        }
    }
}
