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
import org.example.schoolapp.service.role.PrincipleService;

@RestController
@RequestMapping(value = "/api/v1/principal")
@AllArgsConstructor
@Tag(name = "Principle Management", description = "Operations related to principle role.")
public class PrincipleController {
    private PrincipleService principleService;

    @Operation(
            summary = "Get schedule by ID",
            description = "Retrieves the schedule associated with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got the schedule", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
    @GetMapping("/get-schedule/{scheduleId}")
    public ResponseEntity<Response> getScheduleById(@PathVariable Long scheduleId) {
        try {
            return ResponseEntity.ok(new Response("Successfully got Schedule with id", principleService.getScheduleById(scheduleId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Approve schedule by ID",
            description = "Approves the schedule associated with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully approved the schedule", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
    @PutMapping("/approve-schedule/{scheduleId}")
    public ResponseEntity<Response> approveSchedule(@PathVariable Long scheduleId) {
        try {
            return ResponseEntity.ok(new Response("Successfully approved Schedule with id", principleService.approveSchedule(scheduleId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Failed to approve. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Disapprove schedule by ID",
            description = "Disapproves the schedule associated with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully disapproved the schedule", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
    @PutMapping("/disapprove-schedule/{scheduleId}")
    public ResponseEntity<Response> disapproveSchedule(@PathVariable Long scheduleId) {
        try {
            return ResponseEntity.ok(new Response("Successfully disapproved Schedule with id", principleService.disapproveSchedule(scheduleId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Failed to disapprove. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Delete schedule by ID",
            description = "Deletes the schedule associated with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the schedule", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
    @DeleteMapping("/delete-schedule/{scheduleId}")
    public ResponseEntity<Response> deleteSchedule(@PathVariable Long scheduleId) {
        try {
            principleService.deleteSchedule(scheduleId);
            return ResponseEntity.ok(new Response("Successfully deleted Schedule with id", null));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Failed to delete. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Restore schedule by ID",
            description = "Restores the schedule associated with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully restored the schedule", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
    @PutMapping("/restore-schedule/{scheduleId}")
    public ResponseEntity<Response> restoreSchedule(@PathVariable Long scheduleId) {
        try {
            return ResponseEntity.ok(new Response("Successfully restored Schedule with id", principleService.restoreSchedule(scheduleId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Failed to restore. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all schedules",
            description = "Retrieves all schedules."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got all schedules", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Schedules not found", content = @Content)
    })
    @GetMapping("/get-all-schedule")
    public ResponseEntity<Response> getAllSchedule() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all Schedules", principleService.getAllSchedule()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all active schedules",
            description = "Retrieves all schedules that are currently active."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all active schedules", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active schedules found", content = @Content)
    })
    @GetMapping("/get-all-active-schedule")
    public ResponseEntity<Response> getAllActiveSchedule() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all active Schedules", principleService.getAllActiveSchedule()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all active schedules by year",
            description = "Retrieves all active schedules for the specified year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all active schedules for the year", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active schedules found for the specified year", content = @Content)
    })
    @GetMapping("/get-all-active-schedule-by-year/{year}")
    public ResponseEntity<Response> getAllActiveScheduleByYear(@PathVariable String year) {
        try {
            return ResponseEntity.ok(new Response("Successfully got all active Schedules by " + year, principleService.getAllScheduleByYear(year)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Get all unapproved schedules",
            description = "Retrieves all schedules that have not been approved."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all unapproved schedules", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No unapproved schedules found", content = @Content)
    })
    @GetMapping("/get-all-unapproved-schedule")
    public ResponseEntity<Response> getAllUnapprovedSchedule() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all unapproved Schedules", principleService.getAllUnapprovedSchedule()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Create a new schedule",
            description = "Creates a new schedule based on the provided data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created Schedule", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Schedule is not saved", content = @Content)
    })
    @PostMapping(value = "/create-schedule")
    public ResponseEntity<Response> createSchedule(@RequestBody ScheduleDtoRequest request) {
        try {
            ScheduleDto scheduleDto = principleService.createSchedule(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Schedule.", scheduleDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Schedule is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update an existing schedule",
            description = "Updates the schedule with the provided data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Schedule successfully", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Schedule is not updated", content = @Content)
    })
    @PutMapping(value = "/update-schedule")
    public ResponseEntity<Response> updateSchedule(@RequestBody ScheduleDtoRequest request) {
        try {
            ScheduleDto scheduleDto = principleService.updateSchedule(request);
            return ResponseEntity.ok(new Response("Updated Schedule successfully.", scheduleDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Schedule is not updated. " + exception.getMessage(), null));
        }
    }

    @Operation(
            summary = "Hire a new employee",
            description = "Adds a new employee based on the provided data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added Employee", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Employee is not saved", content = @Content)
    })
    @PostMapping(value = "/hire-employee")
    public ResponseEntity<Response> createEmployee(@RequestBody EmployeeDroRequest request) {
        try {
            principleService.hireEmployee(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully added Employee", request));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Employee is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Update an existing employee",
            description = "Updates the details of an existing employee based on the provided EmployeeDroRequest object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Failed to update employee due to bad request.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found.", content = @Content)
    })
    @PutMapping(value = "/update-employee")
    public ResponseEntity<Response> updateEmployee(@RequestBody EmployeeDroRequest request) {
        try {
            EmployeeDto createEmployee = principleService.updateEmployee(request);
            return ResponseEntity.ok(new Response("Updated Employee successfully.", createEmployee));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Employee is not updated. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Retrieve all employees",
            description = "Retrieves a list of all employees in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all employees.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No employees found.", content = @Content)
    })
    @GetMapping("/get-all-employee")
    public ResponseEntity<Response> getAllEmployee() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all Employee.", principleService.getAllEmployee()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Retrieve all active employees",
            description = "Retrieves a list of all active employees in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all active employees.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active employees found.", content = @Content)
    })
    @GetMapping("/get-all-active-employee")
    public ResponseEntity<Response> getAllActiveEmployee() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all active Employee.", principleService.getAllActiveSubject()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Fire an employee",
            description = "Deletes an employee from the system based on the provided employee ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found.", content = @Content)
    })
    @DeleteMapping(value = "/fire-employee/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        try {
            principleService.fireEmployee(employeeId);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to fire. " + exception.getMessage());
        }
    }

    @Operation(summary = "Create a new subject",
            description = "Adds a new subject to the system based on the provided SubjectDtoRequest object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject created successfully.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Failed to save subject due to bad request.", content = @Content)
    })
    @PostMapping(value = "/create-subject")
    public ResponseEntity<Response> createSubject(@RequestBody SubjectDtoRequest request) {
        try {
            principleService.addSubject(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully added Subject.", request));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Subject is not saved. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Update an existing subject",
            description = "Updates the details of an existing subject based on the provided SubjectDtoRequest object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject updated successfully.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Failed to update subject due to bad request.", content = @Content)
    })
    @PutMapping(value = "/update-subject")
    public ResponseEntity<Response> updateSubject(@RequestBody SubjectDtoRequest request) {
        try {
            SubjectDto createSubject = principleService.updateSubject(request);
            return ResponseEntity.ok(new Response("Updated Subject successfully.", createSubject));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Subject is not updated." + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Delete a subject",
            description = "Deletes a subject from the system based on the provided subject ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject deleted successfully.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found.", content = @Content)
    })
    @DeleteMapping(value = "/delete-subject/{id}")
    public ResponseEntity<Response> deleteSubject(@PathVariable Long id) {
        try {
            principleService.deleteSubject(id);
            return ResponseEntity.ok(new Response("Deleted Subject successfully.", null));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Failed to delete. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Restore a deleted subject",
            description = "Restores a previously deleted subject based on the provided subject ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject restored successfully.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found.", content = @Content)
    })
    @PutMapping(value = "/restore-subject/{subjectId}")
    public ResponseEntity<Response> restoreSubject(@PathVariable Long subjectId) {
        try {
            return ResponseEntity.ok(new Response("Restored Subject successfully.", principleService.restoreSubject(subjectId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Failed to restore. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Get all subjects",
            description = "Retrieves a list of all subjects in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all subjects.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No subjects found.", content = @Content)
    })
    @GetMapping(value = "/get-all-subject")
    public ResponseEntity<Response> getAllSubject() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all Subject.", principleService.getAllSubject()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }

    @Operation(summary = "Get all active subjects",
            description = "Retrieves a list of all active subjects currently available in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all active subjects.", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active subjects found.", content = @Content)
    })
    @GetMapping(value = "/get-all-active-subject")
    public ResponseEntity<Response> getAllActiveSubject() {
        try {
            return ResponseEntity.ok(new Response("Successfully got all active Subject.", principleService.getAllActiveSubject()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Couldn't find. " + exception.getMessage(), null));
        }
    }
}
