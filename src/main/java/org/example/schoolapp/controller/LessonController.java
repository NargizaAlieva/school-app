package org.example.schoolapp.controller;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.service.LessonService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "ap1/v1/lesson")
public class LessonController {
    private LessonService lessonService;

    @GetMapping("/get-lesson-by-id/{lessonId}")
    public ResponseEntity<Response> getLessonById(@PathVariable Long lessonId) {
        LessonDto lessonDto = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(new Response("Successfully retrieved Lesson with Id: " + lessonId, lessonDto));
    }

    @GetMapping("/get-all-lesson")
    public ResponseEntity<Response> getAllLesson() {
        List<LessonDto> lessons = lessonService.getAllLesson();

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons.", lessons));
    }

    @GetMapping("/get-all-lesson-by-teacher-id/{teacherId}")
    public ResponseEntity<Response> getAllLessonByTeacherId(@PathVariable Long teacherId) {
        List<LessonDto> lessons = lessonService.getAllLessonByTeacherId(teacherId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with teacherId: " + teacherId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with teacherId: " + teacherId, lessons));
    }

    @GetMapping("/get-all-lesson-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getAllLessonByGradeId(@PathVariable Long gradeId) {
        List<LessonDto> lessons = lessonService.getAllLessonByGradeId(gradeId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with gradeId: " + gradeId, lessons));
    }

    @GetMapping("get-all-lesson-by-subject-id/{subjectId}")
    public ResponseEntity<Response> getAllLessonBySubjectId(@PathVariable Long subjectId) {
        List<LessonDto> lessons = lessonService.getAllLessonBySubjectId(subjectId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with subjectId: " + subjectId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with subjectId: " + subjectId, lessons));
    }

    @GetMapping("get-all-lesson-by-year/{year}")
    public ResponseEntity<Response> getAllLessonByYear(@PathVariable String year) {
        List<LessonDto> lessons = lessonService.getAllLessonByYear(year);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with year: " + year + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with year: " + year, lessons));
    }

    @GetMapping("get-all-lesson-by-quarter/{quarter}")
    public ResponseEntity<Response> getAllLessonByGuarter(@PathVariable Integer quarter) {
        List<LessonDto> lessons = lessonService.getAllLessonByQuarter(quarter);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with quarter: " + quarter + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with quarter: " + quarter, lessons));
    }

    @GetMapping("get-all-lesson-by-subjectId-gradeId/{subjectId}/{gradeId}")
    public ResponseEntity<Response> getAllLessonBySubjectIdAndGradeId(@PathVariable Long subjectId, @PathVariable Long gradeId) {
        List<LessonDto> lessons = lessonService.getAllLessonBySubjectQuarter(subjectId, gradeId);

        if (lessons.isEmpty())
            throw new ObjectNotFoundException("No Lessons with subjectId: " + subjectId + " and gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Lessons with subjectId: " + subjectId
                + " and gradeId: " + gradeId, lessons));
    }

    @PostMapping(value = "/create-lesson")
    public ResponseEntity<Response> createLesson(@RequestBody LessonDtoRequest request) {
        LessonDto lessonDto = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Lesson.", lessonDto));
    }

    @PutMapping(value = "/update-lesson")
    public ResponseEntity<Response> updateLesson(@RequestBody LessonDtoRequest request) {
        LessonDto lessonDto = lessonService.updateLesson(request);
        return ResponseEntity.ok(new Response("Successfully updated Lesson with id: " + request.getId(), lessonDto));
    }
}
