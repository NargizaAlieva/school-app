package org.example.schoolapp.controller;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.Response;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.service.MarkService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "ap1/v1/mark")
public class MarkController {
    private final MarkService markService;

    @GetMapping("/get-mark-by-id/{markId}")
    public ResponseEntity<Response> getMarkById(@PathVariable Long markId) {
        MarkDto markDto = markService.getMarkById(markId);
        return ResponseEntity.ok(new Response("Successfully retrieved Mark with Id: " + markId, markDto));
    }

    @GetMapping("/get-all-mark")
    public ResponseEntity<Response> getAllMark() {
        List<MarkDto> marks = markService.getAllMark();

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks.", marks));
    }

    @GetMapping("/get-all-mark-by-student-id/{studentId}")
    public ResponseEntity<Response> getAllMarkByStudentId(@PathVariable Long studentId) {
        List<MarkDto> marks = markService.getAllMarkByStudent(studentId);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks.", marks));
    }

    @GetMapping("/get-all-mark-by-studentId-subjectId-year-quarter/{studentId}/{subjectId}/{year}/{quarter}")
    public ResponseEntity<Response> getMarksByStudentAndSchedule(@PathVariable Long studentId,
                                                          @PathVariable Long subjectId,
                                                          @PathVariable String year,
                                                          @PathVariable Integer quarter) {
        List<MarkDto> marks = markService.getMarksByStudentAndSchedule(studentId, subjectId, year, quarter);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + " subjectId: "
                    + studentId + ", year: " + year + " quarter : " + quarter + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks with studentId: " + studentId + " subjectId: " +
                + studentId + ", year: " + year + " quarter : " + quarter, marks));
    }

    @GetMapping("/get-all-mark-by-studentId-year-quarter/{studentId}/{year}/{quarter}")
    public ResponseEntity<Response> getMarksByStudentAndSchedule(@PathVariable Long studentId,
                                                                 @PathVariable String year,
                                                                 @PathVariable Integer quarter) {
        List<MarkDto> marks = markService.getMarksByStudentAndSchedule(studentId, year, quarter);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId
                    + ", year: " + year + " quarter : " + quarter + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks with studentId: " + studentId
                + ", year: " + year + " quarter : " + quarter, marks));
    }

    @GetMapping("/get-all-mark-by-studentId-year/{studentId}/{year}")
    public ResponseEntity<Response> getMarksByStudentAndSchedule(@PathVariable Long studentId,
                                                                 @PathVariable String year) {
        List<MarkDto> marks = markService.getMarksByStudentAndSchedule(studentId, year);

        if (marks.isEmpty())
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + ", year: " + year + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Marks with studentId: " + studentId
                + ", year: " + year, marks));
    }

    @PostMapping(value = "/create-mark")
    public ResponseEntity<Response> createMark(@RequestBody MarkDtoRequest request) {
        MarkDto markDto = markService.createMark(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Mark.", markDto));
    }

    @PutMapping(value = "/update-mark")
    public ResponseEntity<Response> updateMark(@RequestBody MarkDtoRequest request) {
        MarkDto markDto = markService.updateMark(request);
        return ResponseEntity.ok(new Response("Successfully updated Mark with id: " + request.getId(), markDto));
    }

    @GetMapping("/get-avg-mark-by-student-id/{studentId}")
    public ResponseEntity<Response> getAvgMarkByStudentId(@PathVariable Long studentId) {
        Double mark = markService.getAverageMarkByStudentId(studentId);
        if (mark == null)
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + " found.");
        return ResponseEntity.ok(new Response("Successfully retrieved average Mark with studentId: " + studentId, mark));
    }

    @GetMapping("/get-avg-mark-by-studentId-subjectId/{studentId}/{subjectId}")
    public ResponseEntity<Response> getAvgMarkByStudentIdSubjectId(@PathVariable Long studentId, @PathVariable Long subjectId) {
        Double mark = markService.getAverageMarkByStudentIdAndSubjectId(studentId, subjectId);
        if (mark == null)
            throw new ObjectNotFoundException("No Marks with studentId: " + studentId + ", subjectId: " + subjectId + " found.");
        return ResponseEntity.ok(new Response("Successfully retrieved average Mark with studentId: " + studentId
                + " and subjectId: " + studentId, mark));
    }
}
