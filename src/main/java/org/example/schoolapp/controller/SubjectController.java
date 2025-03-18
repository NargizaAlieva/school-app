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
import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.service.SubjectService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Subject Management", description = "APIs for managing subjects in the system")
@RequestMapping(value = "ap1/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @Operation(summary = "Get subject by ID", description = "Retrieves a subject by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @GetMapping("/get-subject-by-id/{subjectId}")
    public ResponseEntity<Response> getSubjectById(
            @Parameter(description = "ID of the subject to retrieve", required = true)
            @PathVariable Long subjectId) {
        SubjectDto subjectDto = subjectService.getDtoById(subjectId);
        return ResponseEntity.ok(new Response("Successfully retrieved Subject with Id: " + subjectId, subjectDto));
    }

    @Operation(summary = "Get subject by title", description = "Retrieves a subject by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @GetMapping("/get-subject-by-title/{title}")
    public ResponseEntity<Response> getSubjectById(
            @Parameter(description = "Title of the subject to retrieve", required = true)
            @PathVariable String title) {
        SubjectDto subjectDto = subjectService.getByTitle(title);
        return ResponseEntity.ok(new Response("Successfully retrieved Subject with title: " + title, subjectDto));
    }

    @Operation(summary = "Get all subjects", description = "Retrieves a list of all subjects in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subjects found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No subjects found")
    })
    @GetMapping(value = "/get-all-subject")
    public ResponseEntity<Response> getAllSubject() {
        List<SubjectDto> subjects = subjectService.getAllSubject();

        if (subjects.isEmpty())
            throw new ObjectNotFoundException("No subjects found.");

        return ResponseEntity.ok(new Response("Successfully got all Subjects.", subjects));
    }

    @Operation(summary = "Get all active subjects", description = "Retrieves a list of all active subjects in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active subjects found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active subjects found")
    })
    @GetMapping(value = "/get-all-active-subject")
    public ResponseEntity<Response> getAllActiveSubject() {
        List<SubjectDto> subjects = subjectService.getAllActiveSubject();

        if (subjects.isEmpty())
            throw new ObjectNotFoundException("No subjects found.");

        return ResponseEntity.ok(new Response("Successfully got all active Subjects.", subjects));
    }

    @Operation(summary = "Create a new subject", description = "Creates a new subject in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-subject")
    public ResponseEntity<Response> createSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Subject details to create", required = true,
                    content = @Content(schema = @Schema(implementation = SubjectDtoRequest.class)))
            @RequestBody SubjectDtoRequest request
    ) {
        SubjectDto subjectDto = subjectService.createSubject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Subject.", subjectDto));
    }

    @Operation(summary = "Update a subject", description = "Updates an existing subject in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-subject")
    public ResponseEntity<Response> updateSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Subject details to update", required = true,
                    content = @Content(schema = @Schema(implementation = SubjectDtoRequest.class)))
            @RequestBody SubjectDtoRequest request
    ) {
        SubjectDto createSubject = subjectService.updateSubject(request);
        return ResponseEntity.ok(new Response("Successfully updated Subject with id: " + request.getId(), createSubject));
    }

    @Operation(summary = "Delete a subject", description = "Deletes a subject from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-subject/{subjectId}")
    public ResponseEntity<Response> deleteSubject(
            @Parameter(description = "ID of the subject to delete", required = true)
            @PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(new Response("Successfully deleted Subject with id: " + subjectId, null));
    }

    @Operation(summary = "Restore a subject", description = "Restores a previously deleted subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-subject/{subjectId}")
    public ResponseEntity<Response> restoreSubject(
            @Parameter(description = "ID of the subject to restore", required = true)
            @PathVariable Long subjectId) {
        SubjectDto subjectDto = subjectService.restoreSubject(subjectId);
        return ResponseEntity.ok(new Response("Successfully restored Subject with id: " + subjectId, subjectDto));
    }
}
