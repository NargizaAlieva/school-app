package org.example.schoolapp.controller;

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
@RequestMapping(value = "ap1/v1/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;

    @GetMapping("/get-schedule-by-id/{scheduleId}")
    public ResponseEntity<Response> getScheduleById(@PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(new Response("Successfully retrieved Schedule with Id: " + scheduleId, scheduleDto));
    }

    @GetMapping("/get-all-schedule")
    public ResponseEntity<Response> getAllSchedule() {
        List<ScheduleDto> schedules = scheduleService.getAllSchedule();

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules.", schedules));
    }

    @GetMapping("/get-all-active-schedule")
    public ResponseEntity<Response> getAllActiveSchedule() {
        List<ScheduleDto> schedules = scheduleService.getAllActiveSchedule();

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No active Schedules found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all active Schedules.", schedules));
    }

    @GetMapping("/get-all-unapproved-schedule")
    public ResponseEntity<Response> getAllUnapprovedSchedule() {
        List<ScheduleDto> schedules = scheduleService.getAllUnApprovedSchedule();

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No unapproved Schedules found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all unapproved Schedules.", schedules));
    }

    @GetMapping("/get-schedule-by-teacher-id/{teacherId}")
    public ResponseEntity<Response> getScheduleByTeacherId(@PathVariable Long teacherId) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByTeacher(teacherId);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with teacherId: " + teacherId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with teacherId: " + teacherId, schedules));
    }

    @GetMapping("/get-schedule-by-grade-id/{gradeId}")
    public ResponseEntity<Response> getScheduleByGradeId(@PathVariable Long gradeId) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByGrade(gradeId);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with gradeId: " + gradeId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with gradeId: " + gradeId, schedules));
    }

    @GetMapping("/get-schedule-by-student-id/{studentId}")
    public ResponseEntity<Response> getScheduleByStudentId(@PathVariable Long studentId) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByStudent(studentId);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with studentId: " + studentId + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with studentId: " + studentId, schedules));
    }

    @GetMapping("/get-schedule-by-year/{year}")
    public ResponseEntity<Response> getScheduleByYear(@PathVariable String year) {
        List<ScheduleDto> schedules = scheduleService.getAllScheduleByYear(year);

        if (schedules.isEmpty())
            throw new ObjectNotFoundException("No Schedules with year: " + year + " found.");

        return ResponseEntity.ok(new Response("Successfully retrieved all Schedules with year: " + year, schedules));
    }

    @PostMapping(value = "/create-schedule")
    public ResponseEntity<Response> createSchedule(@RequestBody ScheduleDtoRequest request) {
        ScheduleDto scheduleDto = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Successfully created Schedule.", scheduleDto));
    }

    @PutMapping(value = "/update-schedule")
    public ResponseEntity<Response> updateSchedule(@RequestBody ScheduleDtoRequest request) {
        ScheduleDto scheduleDto = scheduleService.updateSchedule(request);
        return ResponseEntity.ok(new Response("Successfully updated Schedule with id: " + request.getId(), scheduleDto));
    }

    @DeleteMapping(value = "/delete-schedule/{scheduleId}")
    public ResponseEntity<Response> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully deleted Schedule with id: " + scheduleId, null));
    }

    @PutMapping(value = "/restore-schedule/{scheduleId}")
    public ResponseEntity<Response> restoreSchedule(@PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.restoreSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully restored Schedule with id: " + scheduleId, scheduleDto));
    }

    @DeleteMapping(value = "/approve-schedule/{scheduleId}")
    public ResponseEntity<Response> approveSchedule(@PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.approveSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully approve Schedule with id: " + scheduleId, scheduleDto));
    }

    @PutMapping(value = "/disapprove-schedule/{scheduleId}")
    public ResponseEntity<Response> disapproveSchedule(@PathVariable Long scheduleId) {
        ScheduleDto scheduleDto = scheduleService.disapproveSchedule(scheduleId);
        return ResponseEntity.ok(new Response("Successfully disapprove Schedule with id: " + scheduleId, scheduleDto));
    }
}
