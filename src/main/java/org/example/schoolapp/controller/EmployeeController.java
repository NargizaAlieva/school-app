package org.example.schoolapp.controller;

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
@RequestMapping(value = "ap1/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/get-employee-by-id/{employeeId}")
    public ResponseEntity<Response> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeDto employeeDto = employeeService.getDtoById(employeeId);
        return ResponseEntity.ok(new Response("Successfully retrieved Employee with Id: " + employeeId, employeeDto));
    }

    @GetMapping("/get-all-employee")
    public ResponseEntity<Response> getAllEmployee() {
        List<EmployeeDto> employees = employeeService.getAllEmployee();

        if (employees.isEmpty())
            throw new ObjectNotFoundException("No Employees found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Employees.", employees));
    }

    @GetMapping("/get-all-active-employee")
    public ResponseEntity<Response> getAllActiveEmployee() {
        List<EmployeeDto> employees = employeeService.getAllActiveEmployee();

        if (employees.isEmpty())
            throw new ObjectNotFoundException("No active Employees found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Employees.", employees));
    }

    @GetMapping("/get-teacher-by-subject-id/{subjectId}")
    public ResponseEntity<Response> getEmployeeBySubjectId(@PathVariable Long subjectId) {
        List<EmployeeDto> employees = employeeService.getBySubjectId(subjectId);

        if (employees.isEmpty())
            throw new ObjectNotFoundException("No Employees found with subjectId: " + subjectId + ".");

        return ResponseEntity.ok(new Response("Successfully retrieved all Employees with subjectId." + subjectId, employees));
    }

    @GetMapping("/get-home-teacher-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getEmployeeByGradeId(@PathVariable Long gradeId) {
        EmployeeDto employee = employeeService.getHomeTeacherByGradeId(gradeId);
        return ResponseEntity.ok(new Response("Successfully retrieved home teacher of grade with id: " + gradeId, employee));
    }

    @PostMapping(value = "/create-employee")
    public ResponseEntity<Response> createEmployee(@RequestBody @Validated EmployeeDroRequest request) {
        EmployeeDto employeeDto = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Employee.", employeeDto));
    }

    @PutMapping(value = "/update-employee")
    public ResponseEntity<Response> updateEmployee(@RequestBody EmployeeDroRequest request) {
        EmployeeDto createEmployee = employeeService.updateEmployee(request);
        return ResponseEntity.ok(new Response("Successfully updated Employee with id: " + request.getId(), createEmployee));
    }

    @DeleteMapping(value = "/delete-employee/{employeeId}")
    public ResponseEntity<Response> deleteSubject(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(new Response("Successfully deleted Employee with id: " + employeeId, null));
    }

    @PutMapping(value = "/restore-employee/{employeeId}")
    public ResponseEntity<Response> restoreSubject(@PathVariable Long employeeId) {
        EmployeeDto employeeDto = employeeService.restoreEmployee(employeeId);
        return ResponseEntity.ok(new Response("Successfully restored Employee with id: " + employeeId, employeeDto));
    }
}
