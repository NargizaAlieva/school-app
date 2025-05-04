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
import org.example.schoolapp.service.role.TeacherService;

@RestController
@RequestMapping(value = "/api/v1/teacher")
@AllArgsConstructor
@Tag(name = "Teacher Management", description = "Operations related to teacher role.")
public class TeacherController {
    private final TeacherService teacherService;

    @Operation(
            summary = "Get teacher schedule",
            description = "Retrieves the schedule for a specific teacher."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got schedules", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedules not found", content = @Content)
    })
    @GetMapping("/get-schedule")
    public ResponseEntity<Response> getTeacherSchedule() {
        try {
            return ResponseEntity.ok(new Response("Successfully got Schedules.", teacherService.getTeacherSchedule()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get teacher subjects",
            description = "Retrieves the subjects taught by a specific teacher."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got teacher's subjects", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Subjects not found", content = @Content)
    })
    @GetMapping("/get-teacher-subjects")
    public ResponseEntity<Response> getTeacherSubjectList() {
        try {
            return ResponseEntity.ok(new Response("Successfully got teacher's subjects.", teacherService.getTeacherSubjectList()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Create a lesson",
            description = "Creates a new lesson for a specific subject."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created lesson", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Lesson is not saved", content = @Content)
    })
    @PostMapping(value = "/create-lesson")
    public ResponseEntity<Response> createLesson(@RequestBody LessonDtoRequest request) {
        try {
            LessonDto lessonDto = teacherService.createLesson(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Lesson.", lessonDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Lesson is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update a lesson",
            description = "Updates an existing lesson for a specific subject."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated lesson", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Lesson is not updated", content = @Content)
    })
    @PutMapping(value = "/update-lesson")
    public ResponseEntity<Response> updateLesson(@RequestBody LessonDtoRequest request) {
        try {
            LessonDto lessonDto = teacherService.updateLesson(request);
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Successfully updated Lesson.", lessonDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Lesson is not updated. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Create a mark",
            description = "Creates a new mark for a specific student."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created mark", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Mark is not saved", content = @Content)
    })
    @PostMapping(value = "/create-mark")
    public ResponseEntity<Response> createMark(@RequestBody MarkDtoRequest request) {
        try {
            MarkDto markDto = teacherService.createMark(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Mark.", markDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Mark is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update a mark",
            description = "Updates an existing mark for a specific student."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated mark", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Mark is not updated", content = @Content)
    })
    @PutMapping(value = "/update-mark")
    public ResponseEntity<Response> updateMark(@RequestBody MarkDtoRequest request) {
        try {
            MarkDto markDto = teacherService.updateMark(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully updated Mark.", markDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Mark is not updated. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all grades",
            description = "Retrieves all grades available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got all Grades", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Grades not found", content = @Content)
    })
    @GetMapping("/get-all-grade")
    public ResponseEntity<Response> getAllGrade() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all Grades.", teacherService.getAllGrade()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all students by grade",
            description = "Retrieves all students from a specified grade."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got Students from grade", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Students not found", content = @Content)
    })
    @GetMapping("/get-all-student-grade/{gradeId}")
    public ResponseEntity<Response> getAllStudentByGrade(@PathVariable Long gradeId) {
        try {
            return ResponseEntity.ok(new Response("Successfully got Students from grade.", teacherService.getAllStudentByGrade(gradeId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't found. " + exception.getMessage(), null));
        }
    }
}
