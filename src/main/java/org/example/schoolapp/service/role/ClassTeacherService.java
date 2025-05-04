package org.example.schoolapp.service.role;

import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.dto.response.StudentDto;

import java.util.List;
import java.util.Map;

public interface ClassTeacherService {
    Map<String, GradeDto> getTeacherGradesList();

    List<StudentDto> getAllStudentsGrade(Long gradeId);
    List<StudentDto> getAllStudentsFromClass();

    MarkDto createMark(MarkDtoRequest markDtoRequest);
    MarkDto updateMark(MarkDtoRequest markDtoRequest);

    List<StudentDto> getAllStudentHomeGrade(Long gradeId);
}
