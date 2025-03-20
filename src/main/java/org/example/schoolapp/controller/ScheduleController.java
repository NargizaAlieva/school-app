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
import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.service.ScheduleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Schedule Management", description = "APIs for managing schedules in the system")
@RequestMapping(value = "ap1/v1/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;

    @Operation(summary = "Get schedule by ID", description = "Retrieves a schedule by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @GetMapping("/get-schedule-by-id/{scheduleId}")
    public ResponseEntity<Response> getScheduleById(
            @Parameter(description = "ID of the schedule to retrieve", required = true)
            @PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(new Response("Successfully retrieved Schedule with Id: " + scheduleId, scheduleDto));
    }

    @Operation(summary = "Get all schedules", description = "Retrieves a list of all schedules in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No schedules found")
    })
    @GetMapping("/get-all-schedule")
    public ResponseEntity<Response> getAllSchedule() {
        List<ScheduleDto> schedules = scheduleService.getAllSchedule();

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules.", schedules));
    }

    @Operation(summary = "Get all active schedules", description = "Retrieves a list of all active schedules in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active schedules found")
    })
    @GetMapping("/get-all-active-schedule")
    public ResponseEntity<Response> getAllActiveSchedule() {
        List<ScheduleDto> schedules = scheduleService.getAllActiveSchedule();

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No active Schedules found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Schedules.", schedules));
    }

    @Operation(summary = "Get all unapproved schedules", description = "Retrieves a list of all unapproved schedules in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unapproved schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No unapproved schedules found")
    })
    @GetMapping("/get-all-unapproved-schedule")
    public ResponseEntity<Response> getAllUnapprovedSchedule() {
        List<ScheduleDto> schedules = scheduleService.getAllUnApprovedSchedule();

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No unapproved Schedules found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all unapproved Schedules.", schedules));
    }

    @Operation(summary = "Get schedules by teacher ID", description = "Retrieves a list of schedules by teacher ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No schedules found for the given teacher ID")
    })
    @GetMapping("/get-schedule-by-teacher-id/{teacherId}")
    public ResponseEntity<Response> getScheduleByTeacherId(
            @Parameter(description = "ID of the teacher to retrieve schedules for", required = true)
            @PathVariable Long teacherId) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByTeacher(teacherId);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with teacherId: " + teacherId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with teacherId: " + teacherId, schedules));
    }

    @Operation(summary = "Get schedules by grade ID", description = "Retrieves a list of schedules by grade ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No schedules found for the given grade ID")
    })
    @GetMapping("/get-schedule-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getScheduleByGradeId(
            @Parameter(description = "ID of the grade to retrieve schedules for", required = true)
            @PathVariable Long gradeId) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByGrade(gradeId);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with gradeId: " + gradeId, schedules));
    }

    @Operation(summary = "Get schedules by student ID", description = "Retrieves a list of schedules by student ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No schedules found for the given student ID")
    })
    @GetMapping("/get-schedule-by-student-id/{studentId}")
    public ResponseEntity<Response> getScheduleByStudentId(
            @Parameter(description = "ID of the student to retrieve schedules for", required = true)
            @PathVariable Long studentId) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByStudent(studentId);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with studentId: " + studentId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with studentId: " + studentId, schedules));
    }

    @Operation(summary = "Get schedules by year", description = "Retrieves a list of schedules by year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No schedules found for the given year")
    })
    @GetMapping("/get-schedule-by-year/{year}")
    public ResponseEntity<Response> getScheduleByYear(
            @Parameter(description = "Year to retrieve schedules for", required = true)
            @PathVariable String year) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByYear(year);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with year: " + year + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with year: " + year, schedules));
    }

    @Operation(summary = "Create a new schedule", description = "Creates a new schedule in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-schedule")
    public ResponseEntity<Response> createSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Schedule details to create", required = true,
                    content = @Content(schema = @Schema(implementation = ScheduleDtoRequest.class)))
            @RequestBody ScheduleDtoRequest request
    ) {
        ScheduleDto scheduleDto = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Schedule.", scheduleDto));
    }

    @Operation(summary = "Update a schedule", description = "Updates an existing schedule in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-schedule")
    public ResponseEntity<Response> updateSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Schedule details to update", required = true,
                    content = @Content(schema = @Schema(implementation = ScheduleDtoRequest.class)))
            @RequestBody ScheduleDtoRequest request
    ) {
        ScheduleDto scheduleDto = scheduleService.updateSchedule(request);
        return ResponseEntity.ok(new Response("Successfully updated Schedule with id: " + request.getId(), scheduleDto));
    }

    @Operation(summary = "Delete a schedule", description = "Deletes a schedule from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-schedule/{scheduleId}")
    public ResponseEntity<Response> deleteSchedule(
            @Parameter(description = "ID of the schedule to delete", required = true)
            @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully deleted Schedule with id: " + scheduleId, null));
    }

    @Operation(summary = "Restore a schedule", description = "Restores a previously deleted schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-schedule/{scheduleId}")
    public ResponseEntity<Response> restoreSchedule(
            @Parameter(description = "ID of the schedule to restore", required = true)
            @PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.restoreSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully restored Schedule with id: " + scheduleId, scheduleDto));
    }

    @Operation(summary = "Approve a schedule", description = "Approves a schedule in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule approved",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/approve-schedule/{scheduleId}")
    public ResponseEntity<Response> approveSchedule(
            @Parameter(description = "ID of the schedule to approve", required = true)
            @PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.approveSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully approve Schedule with id: " + scheduleId, scheduleDto));
    }

    @Operation(summary = "Disapprove a schedule", description = "Disapproves a schedule in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule disapproved",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/disapprove-schedule/{scheduleId}")
    public ResponseEntity<Response> disapproveSchedule(
            @Parameter(description = "ID of the schedule to disapprove", required = true)
            @PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.disapproveSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully disapprove Schedule with id: " + scheduleId, scheduleDto));
    }
}
