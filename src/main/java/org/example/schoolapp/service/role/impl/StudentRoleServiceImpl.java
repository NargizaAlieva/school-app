package org.example.schoolapp.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.service.entity.*;
import org.example.schoolapp.service.role.StudentRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRoleServiceImpl implements StudentRoleService {
    private final UserService userService;
    private final MarkService markService;
    private final ScheduleService scheduleService;
    private final LessonService lessonService;
    private final StudentService studentService;

    public Student getCurrentStudent() {
        return studentService.getStudentByUserId(userService.getCurrentUser().getId());
    }
    @Override
    public List<MarkDto> getAllMark() {
        return markService.getAllMarkByStudent(getCurrentStudent().getId());
    }

    @Override
    public Double getAvgMarkBySubjectGradeStudent(Long subjectId) {
        return markService.getAverageMarkByStudentIdAndSubjectId(subjectId, getCurrentStudent().getId());
    }

    @Override
    public Double getAvgMarkByGradeStudent() {
        return markService.getAverageMarkByStudentId(getCurrentStudent().getId());
    }

    @Override
    public List<ScheduleDto> getStudentSchedule() {
        return scheduleService.getAllScheduleByStudent(getCurrentStudent().getId());
    }

    @Override
    public List<LessonDto> getAllLessonByGrade() {
        return lessonService.getAllLessonByGradeId(getCurrentStudent().getGrade().getId());
    }

    @Override
    public List<StudentDto> getClassmates() {
        return studentService.getAllStudentByGrade(getCurrentStudent().getGrade().getId());
    }
}
