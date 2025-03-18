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
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.service.MarkService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Mark Management", description = "APIs for managing marks in the system")
@RequestMapping(value = "ap1/v1/mark")
public class MarkController {
    private final MarkService markService;

    @Operation(summary = "Get mark by ID", description = "Retrieves a mark by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mark found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Mark not found")
    })
    @GetMapping("/get-mark-by-id/{markId}")
    public ResponseEntity<Response> getMarkById(
            @Parameter(description = "ID of the mark to retrieve", required = true)
            @PathVariable Long markId) {
        MarkDto markDto = markService.getMarkById(markId);
        return ResponseEntity.ok(new Response("Successfully retrieved Mark with Id: " + markId, markDto));
    }

    @Operation(summary = "Get all marks", description = "Retrieves a list of all marks in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marks found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found")
    })
    @GetMapping("/get-all-mark")
    public ResponseEntity<Response> getAllMark() {
        List<MarkDto> marks = markService.getAllMark();

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks.", marks));
    }

    @Operation(summary = "Get all marks by student ID", description = "Retrieves a list of marks for a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marks found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found for the given student ID")
    })
    @GetMapping("/get-all-mark-by-student-id/{studentId}")
    public ResponseEntity<Response> getAllMarkByStudentId(
            @Parameter(description = "ID of the student to retrieve marks for", required = true)
            @PathVariable Long studentId) {
        List<MarkDto> marks = markService.getAllMarkByStudent(studentId);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks.", marks));
    }

    @Operation(summary = "Get marks by student ID, subject ID, year, and quarter",
            description = "Retrieves a list of marks for a specific student, subject, year, and quarter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marks found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found for the given criteria")
    })
    @GetMapping("/get-all-mark-by-studentId-subjectId-year-quarter/{studentId}/{subjectId}/{year}/{quarter}")
    public ResponseEntity<Response> getMarksByStudentAndSchedule(
            @Parameter(description = "ID of the student", required = true) @PathVariable Long studentId,
            @Parameter(description = "ID of the subject", required = true) @PathVariable Long subjectId,
            @Parameter(description = "Year of the marks", required = true) @PathVariable String year,
            @Parameter(description = "Quarter of the marks", required = true) @PathVariable Integer quarter
    ) {
        List<MarkDto> marks = markService.getMarksByStudentAndSchedule(studentId, subjectId, year, quarter);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + " subjectId: "
                    + studentId + ", year: " + year + " quarter : " + quarter + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks with studentId: " + studentId + " subjectId: " +
                + studentId + ", year: " + year + " quarter : " + quarter, marks));
    }

    @Operation(summary = "Get marks by student ID, year, and quarter",
            description = "Retrieves a list of marks for a specific student, year, and quarter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marks found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found for the given criteria")
    })
    @GetMapping("/get-all-mark-by-studentId-year-quarter/{studentId}/{year}/{quarter}")
    public ResponseEntity<Response> getMarksByStudentAndSchedule(
            @Parameter(description = "ID of the student", required = true) @PathVariable Long studentId,
            @Parameter(description = "Year of the marks", required = true) @PathVariable String year,
            @Parameter(description = "Quarter of the marks", required = true) @PathVariable Integer quarter
    ) {
        List<MarkDto> marks = markService.getMarksByStudentAndSchedule(studentId, year, quarter);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId
                    + ", year: " + year + " quarter : " + quarter + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks with studentId: " + studentId
                + ", year: " + year + " quarter : " + quarter, marks));
    }

    @Operation(summary = "Get marks by student ID and year",
            description = "Retrieves a list of marks for a specific student and year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marks found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found for the given criteria")
    })
    @GetMapping("/get-all-mark-by-studentId-year/{studentId}/{year}")
    public ResponseEntity<Response> getMarksByStudentAndSchedule(
            @Parameter(description = "ID of the student", required = true) @PathVariable Long studentId,
            @Parameter(description = "Year of the marks", required = true) @PathVariable String year
    ) {
        List<MarkDto> marks = markService.getMarksByStudentAndSchedule(studentId, year);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + ", year: " + year + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks with studentId: " + studentId
                + ", year: " + year, marks));
    }

    @Operation(summary = "Create a new mark", description = "Creates a new mark in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mark created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-mark")
    public ResponseEntity<Response> createMark(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Mark details to create", required = true,
                    content = @Content(schema = @Schema(implementation = MarkDtoRequest.class)))
            @RequestBody MarkDtoRequest request
    ) {
        MarkDto markDto = markService.createMark(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Mark.", markDto));
    }

    @Operation(summary = "Update a mark", description = "Updates an existing mark in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mark updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-mark")
    public ResponseEntity<Response> updateMark(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Mark details to update", required = true,
                    content = @Content(schema = @Schema(implementation = MarkDtoRequest.class)))
            @RequestBody MarkDtoRequest request
    ) {
        MarkDto markDto = markService.updateMark(request);
        return ResponseEntity.ok(new Response("Successfully updated Mark with id: " + request.getId(), markDto));
    }

    @Operation(summary = "Get average mark by student ID", description = "Retrieves the average mark for a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average mark found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found for the given student ID")
    })
    @GetMapping("/get-avg-mark-by-student-id/{studentId}")
    public ResponseEntity<Response> getAvgMarkByStudentId(
            @Parameter(description = "ID of the student to retrieve average mark for", required = true)
            @PathVariable Long studentId) {
        Double mark = markService.getAverageMarkByStudentId(studentId);
        if (mark == null)
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + " found.");
        return ResponseEntity.ok(new Response("Successfully retrieved average Mark with studentId: " + studentId, mark));
    }

    @Operation(summary = "Get average mark by student ID and subject ID",
            description = "Retrieves the average mark for a specific student and subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average mark found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No marks found for the given student ID and subject ID")
    })
    @GetMapping("/get-avg-mark-by-studentId-subjectId/{studentId}/{subjectId}")
    public ResponseEntity<Response> getAvgMarkByStudentIdSubjectId(
            @Parameter(description = "ID of the student", required = true) @PathVariable Long studentId,
            @Parameter(description = "ID of the subject", required = true) @PathVariable Long subjectId
    ) {
        Double mark = markService.getAverageMarkByStudentIdAndSubjectId(studentId, subjectId);
        if (mark == null)
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + ", subjectId: " + subjectId + " found.");
        return ResponseEntity.ok(new Response("Successfully retrieved average Mark with studentId: " + studentId
                + " and subjectId: " + studentId, mark));
    }
}
