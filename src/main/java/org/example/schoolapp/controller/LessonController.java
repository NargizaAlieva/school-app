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
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.service.LessonService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Lesson Management", description = "APIs for managing lessons in the system")
@RequestMapping(value = "ap1/v1/lesson")
public class LessonController {
    private LessonService lessonService;

    @Operation(summary = "Get lesson by ID", description = "Retrieves a lesson by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @GetMapping("/get-lesson-by-id/{lessonId}")
    public ResponseEntity<Response> getLessonById(
            @Parameter(description = "ID of the lesson to retrieve", required = true)
            @PathVariable Long lessonId) {
        LessonDto lessonDto = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(new Response("Successfully retrieved Lesson with Id: " + lessonId, lessonDto));
    }

    @Operation(summary = "Get all lessons", description = "Retrieves a list of all lessons in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found")
    })
    @GetMapping("/get-all-lesson")
    public ResponseEntity<Response> getAllLesson() {
        List<LessonDto> lessons = lessonService.getAllLesson();

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons.", lessons));
    }

    @Operation(summary = "Get all lessons by teacher ID", description = "Retrieves a list of lessons for a specific teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the given teacher ID")
    })
    @GetMapping("/get-all-lesson-by-teacher-id/{teacherId}")
    public ResponseEntity<Response> getAllLessonByTeacherId(
            @Parameter(description = "ID of the teacher to retrieve lessons for", required = true)
            @PathVariable Long teacherId) {
        List<LessonDto> lessons = lessonService.getAllLessonByTeacherId(teacherId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with teacherId: " + teacherId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with teacherId: " + teacherId, lessons));
    }

    @Operation(summary = "Get all lessons by grade ID", description = "Retrieves a list of lessons for a specific grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the given grade ID")
    })
    @GetMapping("/get-all-lesson-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getAllLessonByGradeId(
            @Parameter(description = "ID of the grade to retrieve lessons for", required = true)
            @PathVariable Long gradeId) {
        List<LessonDto> lessons = lessonService.getAllLessonByGradeId(gradeId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with gradeId: " + gradeId, lessons));
    }

    @Operation(summary = "Get all lessons by subject ID", description = "Retrieves a list of lessons for a specific subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the given subject ID")
    })
    @GetMapping("get-all-lesson-by-subject-id/{subjectId}")
    public ResponseEntity<Response> getAllLessonBySubjectId(
            @Parameter(description = "ID of the subject to retrieve lessons for", required = true)
            @PathVariable Long subjectId) {
        List<LessonDto> lessons = lessonService.getAllLessonBySubjectId(subjectId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with subjectId: " + subjectId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with subjectId: " + subjectId, lessons));
    }

    @Operation(summary = "Get all lessons by year", description = "Retrieves a list of lessons for a specific year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the given year")
    })
    @GetMapping("get-all-lesson-by-year/{year}")
    public ResponseEntity<Response> getAllLessonByYear(
            @Parameter(description = "Year to retrieve lessons for", required = true)
            @PathVariable String year) {
        List<LessonDto> lessons = lessonService.getAllLessonByYear(year);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with year: " + year + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with year: " + year, lessons));
    }

    @Operation(summary = "Get all lessons by quarter", description = "Retrieves a list of lessons for a specific quarter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the given quarter")
    })
    @GetMapping("get-all-lesson-by-quarter/{quarter}")
    public ResponseEntity<Response> getAllLessonByQuarter(
            @Parameter(description = "Quarter to retrieve lessons for", required = true)
            @PathVariable Integer quarter) {
        List<LessonDto> lessons = lessonService.getAllLessonByQuarter(quarter);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with quarter: " + quarter + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with quarter: " + quarter, lessons));
    }

    @Operation(summary = "Get all lessons by subject ID and grade ID",
            description = "Retrieves a list of lessons for a specific subject and grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No lessons found for the given subject ID and grade ID")
    })
    @GetMapping("get-all-lesson-by-subjectId-gradeId/{subjectId}/{gradeId}")
    public ResponseEntity<Response> getAllLessonBySubjectIdAndGradeId(
            @Parameter(description = "ID of the subject", required = true) @PathVariable Long subjectId,
            @Parameter(description = "ID of the grade", required = true) @PathVariable Long gradeId
    ) {
        List<LessonDto> lessons = lessonService.getAllLessonBySubjectQuarter(subjectId, gradeId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with subjectId: " + subjectId + " and gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with subjectId: " + subjectId
                + " and gradeId: " + gradeId, lessons));
    }

    @Operation(summary = "Create a new lesson", description = "Creates a new lesson in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-lesson")
    public ResponseEntity<Response> createLesson(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lesson details to create", required = true,
                    content = @Content(schema = @Schema(implementation = LessonDtoRequest.class)))
            @RequestBody LessonDtoRequest request
    ) {
        LessonDto lessonDto = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Lesson.", lessonDto));
    }

    @Operation(summary = "Update a lesson", description = "Updates an existing lesson in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-lesson")
    public ResponseEntity<Response> updateLesson(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lesson details to update", required = true,
                    content = @Content(schema = @Schema(implementation = LessonDtoRequest.class)))
            @RequestBody LessonDtoRequest request
    ) {
        LessonDto lessonDto = lessonService.updateLesson(request);
        return ResponseEntity.ok(new Response("Successfully updated Lesson with id: " + request.getId(), lessonDto));
    }
}
