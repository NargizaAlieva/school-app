package org.example.schoolapp.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.service.entity.*;
import org.example.schoolapp.service.role.ClassTeacherService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.GradeMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClassTeacherServiceImpl implements ClassTeacherService {
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final MarkService markService;
    private final GradeMapper gradeMapper;

    private Employee getCurrentClassTeacher() {
        return employeeService.findByUserId(userService.getCurrentUser().getId());
    }

    public Grade getTeacherGrades(Long subjectId) {
        if (getCurrentClassTeacher().getHomeGrades().size() < subjectId)
            throw new ObjectNotFoundException("Subject");
        return getCurrentClassTeacher().getHomeGrades().get((int) (subjectId-1));
    }

    public Boolean isStudentFromTeacherGrade(Long studentId) {
        return getCurrentClassTeacher().getHomeGrades().contains(studentService.getStudentByIdEntity(studentId).getGrade());
    }

    @Override
    public Map<String, GradeDto> getTeacherGradesList() {
        Map<String, GradeDto> studentDtoMap = new HashMap<>();
        for (int i = 1; i <= getCurrentClassTeacher().getHomeGrades().size(); i++) {
            String gradeIndex = "Grade: " + i;
            GradeDto gradeDto = gradeMapper.toGradeDto(getTeacherGrades((long) i));
            studentDtoMap.put(gradeIndex, gradeDto);
        }
        return studentDtoMap;
    }

    @Override
    public List<StudentDto> getAllStudentsGrade(Long gradeId) {
        Grade grade = getTeacherGrades(gradeId);
        return studentService.getAllStudentByGrade(grade.getId());
    }

    @Override
    public List<StudentDto> getAllStudentsFromClass() {
        return studentService.getAllStudentByGrades(employeeService.getHomeClassesId());
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
    public List<StudentDto> getAllStudentHomeGrade(Long gradeId) {
        for (Grade g : getCurrentClassTeacher().getHomeGrades())
            if (g.getId().equals(gradeId))
                return getAllStudentsGrade(g.getId());
        throw new ObjectNotFoundException("Home Grade");
    }
}
