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
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.service.StudentService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Student Management", description = "APIs for managing students in the system")
@RequestMapping(value = "ap1/v1/student")
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "Get student by ID", description = "Retrieves a student by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/get-student-by-id/{studentId}")
    public ResponseEntity<Response> getStudentById(
            @Parameter(description = "ID of the student to retrieve", required = true)
            @PathVariable Long studentId) {
        StudentDto studentDto = studentService.getStudentById(studentId);
        return ResponseEntity.ok(new Response("Successfully retrieved Student with Id: " + studentId, studentDto));
    }

    @Operation(summary = "Get all students", description = "Retrieves a list of all students in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No students found")
    })
    @GetMapping("/get-all-student")
    public ResponseEntity<Response> getAllStudent() {
        List<StudentDto> students = studentService.getAllStudent();

        if (students.isEmpty())
            throw new ObjectNotFoundException("No Students found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Students.", students));
    }

    @Operation(summary = "Get all active students", description = "Retrieves a list of all active students in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active students found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No active students found")
    })
    @GetMapping("/get-all-active-student")
    public ResponseEntity<Response> getAllActiveStudent() {
        List<StudentDto> students = studentService.getAllActiveStudent();

        if (students.isEmpty())
            throw new ObjectNotFoundException("No active Students found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Students.", students));
    }

    @Operation(summary = "Get students by parent ID", description = "Retrieves a list of students by their parent's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No students found for the given parent ID")
    })
    @GetMapping("/get-students-by-parent-id/{parentId}")
    public ResponseEntity<Response> getStudentByParentd(
            @Parameter(description = "ID of the parent to retrieve students for", required = true)
            @PathVariable Long parentId) {
        List<StudentDto> students = studentService.getStudentByParentId(parentId);

        if (students.isEmpty())
            throw new ObjectNotFoundException("No Students with gradeId: " + parentId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Students.", students));
    }

    @Operation(summary = "Get students by grade ID", description = "Retrieves a list of students by their grade's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No students found for the given grade ID")
    })
    @GetMapping("/get-students-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getStudentByGradeId(
            @Parameter(description = "ID of the grade to retrieve students for", required = true)
            @PathVariable Long gradeId) {
        List<StudentDto> students = studentService.getAllStudentByGrade(gradeId);

        if (students.isEmpty())
            throw new ObjectNotFoundException("No Students with gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Students.", students));
    }

    @Operation(summary = "Create a new student", description = "Creates a new student in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping(value = "/create-student")
    public ResponseEntity<Response> createStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Student details to create", required = true,
                    content = @Content(schema = @Schema(implementation = StudentDtoRequest.class)))
            @RequestBody StudentDtoRequest request
    ) {
        StudentDto studentDto = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Student.", studentDto));
    }

    @Operation(summary = "Update a student", description = "Updates an existing student in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/update-student")
    public ResponseEntity<Response> updateStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Student details to update", required = true,
                    content = @Content(schema = @Schema(implementation = StudentDtoRequest.class)))
            @RequestBody StudentDtoRequest request
    ) {
        StudentDto studentDto = studentService.updateStudent(request);
        return ResponseEntity.ok(new Response("Successfully updated Student with id: " + request.getId(), studentDto));
    }

    @Operation(summary = "Delete a student", description = "Deletes a student from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(value = "/delete-student/{studentId}")
    public ResponseEntity<Response> deleteStudent(
            @Parameter(description = "ID of the student to delete", required = true)
            @PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(new Response("Successfully deleted Student with id: " + studentId, null));
    }

    @Operation(summary = "Restore a student", description = "Restores a previously deleted student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student restored",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping(value = "/restore-student/{studentId}")
    public ResponseEntity<Response> restoreStudent(
            @Parameter(description = "ID of the student to restore", required = true)
            @PathVariable Long studentId) {
        StudentDto studentDto = studentService.restoreStudent(studentId);
        return ResponseEntity.ok(new Response("Successfully restored Student with id: " + studentId, studentDto));
    }
}
