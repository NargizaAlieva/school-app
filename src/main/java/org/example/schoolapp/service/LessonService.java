package org.example.schoolapp.service;

import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.entity.Lesson;

import java.util.List;
import java.util.Map;

public interface LessonService {
    Lesson save(Lesson lesson);

    Lesson getLessonByIdEntity(Long id);
    LessonDto getLessonById(Long id);
    LessonDto createLesson(LessonDtoRequest lessonDtoRequest);
    LessonDto updateLesson(LessonDtoRequest lessonDtoRequest);
    List<LessonDto> getAllLesson();

    List<LessonDto> getAllLessonByTeacherId(Long teacherId);

    List<LessonDto> getAllLessonByGradeId(Long gradeId);

    List<LessonDto> getAllLessonBySubjectId(Long subjectId);

    List<LessonDto> getAllLessonByYear(String year);

    List<LessonDto> getAllLessonByQuarter(Integer quarter);

    List<LessonDto> getAllLessonBySubjectQuarter(Long subjectId, Long gradeId);
}
