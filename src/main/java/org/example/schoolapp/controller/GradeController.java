package org.example.schoolapp.controller;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "ap1/v1/grade")
public class GradeController {
    private final GradeService gradeService;

    @GetMapping("/get-grade-by-id/{gradeId}")
    public ResponseEntity<Response> getGradeById(@PathVariable Long gradeId) {
        GradeDto gradeDto = gradeService.getById(gradeId);
        return ResponseEntity.ok(new Response("Successfully retrieved Grade with Id: " + gradeId, gradeDto));
    }

    @GetMapping("/get-all-grade")
    public ResponseEntity<Response> getAllGrade() {
        List<GradeDto> grades = gradeService.getAllGrade();

        if (grades.isEmpty())
            throw new ObjectNotFoundException("No Grades found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Grades.", grades));
    }

    @GetMapping("/get-all-active-grade")
    public ResponseEntity<Response> getAllActiveGrade() {
        List<GradeDto> grades = gradeService.getAllActiveGrade();

        if (grades.isEmpty())
            throw new ObjectNotFoundException("No active Grades found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Grades.", grades));
    }

    @GetMapping("/get-grade-by-teacherId/{teacherId}")
    public ResponseEntity<Response> getGradeByTeacherId(@PathVariable Long teacherId) {
        List<GradeDto> grades = gradeService.getAllGradeByTeacherId(teacherId);

        if (grades.isEmpty())
            throw new ObjectNotFoundException("No Grades with home teacher with id: " + teacherId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Grades with home teacher with id: " + teacherId, grades));
    }

    @GetMapping("/get-grade-by-teacherId/{studentId}")
    public ResponseEntity<Response> getGradeByStudentId(@PathVariable Long studentId) {
        GradeDto gradeDto = gradeService.getGradeByStudentId(studentId);
        return ResponseEntity.ok(new Response("Successfully retrieved Grade with student by id: " + studentId, gradeDto));
    }

    @PostMapping(value = "/create-grade")
    public ResponseEntity<Response> createGrade(@RequestBody GradeDtoRequest request) {
        GradeDto gradeDto = gradeService.createGrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Grade.", gradeDto));
    }

    @PutMapping(value = "/update-grade")
    public ResponseEntity<Response> updateGrade(@RequestBody GradeDtoRequest request) {
        GradeDto gradeDto = gradeService.updateGrade(request);
        return ResponseEntity.ok(new Response("Successfully updated Grade with id: " + request.getId(), gradeDto));
    }

    @DeleteMapping(value = "/delete-grade/{gradeId}")
    public ResponseEntity<Response> deleteGrade(@PathVariable Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return ResponseEntity.ok(new Response("Successfully deleted Grade with id: " + gradeId, null));
    }

    @PutMapping(value = "/restore-grade/{gradeId}")
    public ResponseEntity<Response> restoreGrade(@PathVariable Long gradeId) {
        GradeDto gradeDto = gradeService.restoreGrade(gradeId);
        return ResponseEntity.ok(new Response("Successfully restored Grade with id: " + gradeId, gradeDto));
    }
}
