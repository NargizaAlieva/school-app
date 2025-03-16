package org.example.schoolapp.controller;

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
@RequestMapping(value = "ap1/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/get-subject-by-id/{subjectId}")
    public ResponseEntity<Response> getSubjectById(@PathVariable Long subjectId) {
        SubjectDto subjectDto = subjectService.getDtoById(subjectId);
        return ResponseEntity.ok(new Response("Successfully retrieved Subject with Id: " + subjectId, subjectDto));
    }

    @GetMapping("/get-subject-by-title/{title}")
    public ResponseEntity<Response> getSubjectById(@PathVariable String title) {
        SubjectDto subjectDto = subjectService.getByTitle(title);
        return ResponseEntity.ok(new Response("Successfully retrieved Subject with title: " + title, subjectDto));
    }

    @GetMapping(value = "/get-all-subject")
    public ResponseEntity<Response> getAllSubject() {
        List<SubjectDto> subjects = subjectService.getAllSubject();

        if (subjects.isEmpty())
            throw new ObjectNotFoundException("No subjects found.");

        return ResponseEntity.ok(new Response("Successfully got all Subjects.", subjects));
    }

    @GetMapping(value = "/get-all-active-subject")
    public ResponseEntity<Response> getAllActiveSubject() {
        List<SubjectDto> subjects = subjectService.getAllActiveSubject();

        if (subjects.isEmpty())
            throw new ObjectNotFoundException("No subjects found.");

        return ResponseEntity.ok(new Response("Successfully got all active Subjects.", subjects));
    }

    @PostMapping(value = "/create-subject")
    public ResponseEntity<Response> createSubject(@RequestBody SubjectDtoRequest request) {
        SubjectDto subjectDto = subjectService.createSubject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Subject.", subjectDto));
    }

    @PutMapping(value = "/update-subject")
    public ResponseEntity<Response> updateSubject(@RequestBody SubjectDtoRequest request) {
        SubjectDto createSubject = subjectService.updateSubject(request);
        return ResponseEntity.ok(new Response("Successfully updated Subject with id: " + request.getId(), createSubject));
    }

    @DeleteMapping(value = "/delete-subject/{subjectId}")
    public ResponseEntity<Response> deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(new Response("Successfully deleted Subject with id: " + subjectId, null));
    }

    @PutMapping(value = "/restore-subject/{subjectId}")
    public ResponseEntity<Response> restoreSubject(@PathVariable Long subjectId) {
        SubjectDto subjectDto = subjectService.restoreSubject(subjectId);
        return ResponseEntity.ok(new Response("Successfully restored Subject with id: " + subjectId, subjectDto));
    }
}
