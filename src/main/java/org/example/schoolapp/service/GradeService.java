package org.example.schoolapp.service;

import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.entity.Grade;

import java.util.List;
import java.util.Set;

public interface GradeService {
    Grade save(Grade grade);

    Grade getByIdEntity(Long id);
    GradeDto getById(Long id);

    List<GradeDto> getAllGrade();
    List<GradeDto> getAllActiveGrade();

    List<GradeDto> getAllGradeByTeacherId(Long teacherId);
    GradeDto getGradeByStudentId(Long studentId);

    GradeDto createGrade(GradeDtoRequest gradeDtoRequest);
    GradeDto updateGrade(GradeDtoRequest gradeDtoRequest);

    GradeDto deleteGrade(Long id);
    GradeDto restoreGrade(Long id);
}
