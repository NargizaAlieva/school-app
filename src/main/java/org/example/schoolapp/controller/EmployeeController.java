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
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.response.EmployeeDto;
import org.example.schoolapp.service.EmployeeService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Employee Management", description = "APIs for managing employees in the system")
@RequestMapping(value = "ap1/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/get-employee-by-id/{employeeId}")
    public ResponseEntity<Response> getEmployeeById(
            @Parameter(description = "ID of the employee to retrieve", required = true)
            @PathVariable Long employeeId) {
        EmployeeDto employeeDto = employeeService.getDtoById(employeeId);
        return ResponseEntity.ok(new Response("Successfully retrieved Employee with Id: " + employeeId, employeeDto));
    }

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No employees found")
    })
    @GetMapping("/get-all-employee")
    public ResponseEntity<Response> getAllEmployee() {
        List<EmployeeDto> employees = employeeService.getAllEmployee();

        if (employees.isEmpty())
            throw new ObjectNotFoundException("No Employees found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Employees.", employees));
    }

    @Operation(summary = "Get all active employees", description = "Retrieves a list of all active employees in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active employees found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active employees found")
    })
    @GetMapping("/get-all-active-employee")
    public ResponseEntity<Response> getAllActiveEmployee() {
        List<EmployeeDto> employees = employeeService.getAllActiveEmployee();

        if (employees.isEmpty())
            throw new ObjectNotFoundException("No active Employees found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Employees.", employees));
    }

    @Operation(summary = "Get employees by subject ID", description = "Retrieves a list of employees for a specific subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No employees found for the given subject ID")
    })
    @GetMapping("/get-teacher-by-subject-id/{subjectId}")
    public ResponseEntity<Response> getEmployeeBySubjectId(
            @Parameter(description = "ID of the subject to retrieve employees for", required = true)
            @PathVariable Long subjectId) {
        List<EmployeeDto> employees = employeeService.getBySubjectId(subjectId);

        if (employees.isEmpty())
            throw new ObjectNotFoundException("No Employees found with subjectId: " + subjectId + ".");

        return ResponseEntity.ok(new Response("Successfully retrieved all Employees with subjectId." + subjectId, employees));
    }

    @Operation(summary = "Get home teacher by grade ID", description = "Retrieves the home teacher for a specific grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Home teacher found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No home teacher found for the given grade ID")
    })
    @GetMapping("/get-home-teacher-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getEmployeeByGradeId(
            @Parameter(description = "ID of the grade to retrieve home teacher for", required = true)
            @PathVariable Long gradeId) {
        EmployeeDto employee = employeeService.getHomeTeacherByGradeId(gradeId);
        return ResponseEntity.ok(new Response("Successfully retrieved home teacher of grade with id: " + gradeId, employee));
    }

    @Operation(summary = "Create a new employee", description = "Creates a new employee in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-employee")
    public ResponseEntity<Response> createEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Employee details to create", required = true,
                    content = @Content(schema = @Schema(implementation = EmployeeDroRequest.class)))
            @RequestBody @Validated EmployeeDroRequest request
    ) {
        EmployeeDto employeeDto = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Employee.", employeeDto));
    }

    @Operation(summary = "Update an employee", description = "Updates an existing employee in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-employee")
    public ResponseEntity<Response> updateEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Employee details to update", required = true,
                    content = @Content(schema = @Schema(implementation = EmployeeDroRequest.class)))
            @RequestBody EmployeeDroRequest request
    ) {
        EmployeeDto createEmployee = employeeService.updateEmployee(request);
        return ResponseEntity.ok(new Response("Successfully updated Employee with id: " + request.getId(), createEmployee));
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-employee/{employeeId}")
    public ResponseEntity<Response> deleteSubject(
            @Parameter(description = "ID of the employee to delete", required = true)
            @PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(new Response("Successfully deleted Employee with id: " + employeeId, null));
    }

    @Operation(summary = "Restore an employee", description = "Restores a previously deleted employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-employee/{employeeId}")
    public ResponseEntity<Response> restoreSubject(
            @Parameter(description = "ID of the employee to restore", required = true)
            @PathVariable Long employeeId) {
        EmployeeDto employeeDto = employeeService.restoreEmployee(employeeId);
        return ResponseEntity.ok(new Response("Successfully restored Employee with id: " + employeeId, employeeDto));
    }
}
