package org.example.schoolapp.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.service.entity.*;
import org.example.schoolapp.service.role.TeacherService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.SubjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final ScheduleService scheduleService;
    private final MarkService markService;
    private final GradeService gradeService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final LessonService lessonService;
    private final SubjectMapper subjectMapper;

    public Employee getCurrentTeacher() {
        return employeeService.findByUserId(userService.getCurrentUser().getId());
    }
    public Subject getTeacherSubjects(Long subjectId) {
        List<Subject> subjectList = new ArrayList<>(getCurrentTeacher().getSubjectSet());
        if (subjectList.size() < subjectId)
            throw new ObjectNotFoundException("Subject");
        return subjectList.get((int) (subjectId-1));
    }

    @Override
    public Map<String, SubjectDto> getTeacherSubjectList() {
        Map<String, SubjectDto> studentDtoMap = new HashMap<>();
        for (int i = 1; i <= getCurrentTeacher().getSubjectSet().size(); i++) {
            String subjectIndex = "Subject: " + i;
            SubjectDto subjectDto = subjectMapper.toSubjectDto(getTeacherSubjects((long) i));
            studentDtoMap.put(subjectIndex, subjectDto);
        }
        return studentDtoMap;
    }

    @Override
    public List<ScheduleDto> getTeacherSchedule() {
        return scheduleService.getAllScheduleByTeacher(getCurrentTeacher().getId());
    }

    @Override
    public LessonDto createLesson(LessonDtoRequest lessonDtoRequest) {
        return lessonService.createLesson(lessonDtoRequest);
    }

    @Override
    public LessonDto updateLesson(LessonDtoRequest lessonDtoRequest) {
        return lessonService.updateLesson(lessonDtoRequest);
    }

    @Override
    public MarkDto createMark(MarkDtoRequest markDtoRequest) {
        return markService.createMark(markDtoRequest);
    }

    @Override
    public MarkDto updateMark(MarkDtoRequest markDtoRequest) {
        return markService.updateMark(markDtoRequest);
    }

    @Override
    public List<GradeDto> getAllGrade() {
        return gradeService.getAllGrade();
    }

    @Override
    public List<StudentDto> getAllStudentByGrade(Long gradeId) {
        return studentService.getAllStudentByGrade(gradeId);
    }
}
