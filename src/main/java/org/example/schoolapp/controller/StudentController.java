package org.example.schoolapp.controller;

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
@RequestMapping(value = "ap1/v1/student")
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/get-student-by-id/{studentId}")
    public ResponseEntity<Response> getStudentById(@PathVariable Long studentId) {
        StudentDto studentDto = studentService.getStudentById(studentId);
        return ResponseEntity.ok(new Response("Successfully retrieved Student with Id: " + studentId, studentDto));
    }

    @GetMapping("/get-all-student")
    public ResponseEntity<Response> getAllStudent() {
        List<StudentDto> students = studentService.getAllStudent();

        if (students.isEmpty())
            throw new ObjectNotFoundException("No Students found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Students.", students));
    }

    @GetMapping("/get-all-active-student")
    public ResponseEntity<Response> getAllActiveStudent() {
        List<StudentDto> students = studentService.getAllActiveStudent();

        if (students.isEmpty())
            throw new ObjectNotFoundException("No active Students found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Students.", students));
    }

    @GetMapping("/get-students-by-parent-id/{parentId}")
    public ResponseEntity<Response> getStudentByParentd(@RequestBody Long parentId) {
        List<StudentDto> students = studentService.getStudentByParentId(parentId);

        if (students.isEmpty())
            throw new ObjectNotFoundException("No Students with gradeId: " + parentId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Students.", students));
    }

    @GetMapping("/get-students-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getStudentByGradeId(@RequestBody Long gradeId) {
        List<StudentDto> students = studentService.getAllStudentByGrade(gradeId);

        if (students.isEmpty())
            throw new ObjectNotFoundException("No Students with gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Students.", students));
    }

    @PostMapping(value = "/create-student")
    public ResponseEntity<Response> createStudent(@RequestBody StudentDtoRequest request) {
        StudentDto studentDto = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Student.", studentDto));
    }

    @PutMapping(value = "/update-student")
    public ResponseEntity<Response> updateStudent(@RequestBody StudentDtoRequest request) {
        StudentDto studentDto = studentService.updateStudent(request);
        return ResponseEntity.ok(new Response("Successfully updated Student with id: " + request.getId(), studentDto));
    }

    @DeleteMapping(value = "/delete-student/{studentId}")
    public ResponseEntity<Response> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(new Response("Successfully deleted Student with id: " + studentId, null));
    }

    @PutMapping(value = "/restore-student/{studentId}")
    public ResponseEntity<Response> restoreStudent(@PathVariable Long studentId) {
        StudentDto studentDto = studentService.restoreStudent(studentId);
        return ResponseEntity.ok(new Response("Successfully restored Student with id: " + studentId, studentDto));
    }
}
