package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.entity.Lesson;
import org.example.schoolapp.repository.LessonRepository;
import org.example.schoolapp.service.LessonService;
import org.example.schoolapp.service.ScheduleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.LessonMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final ScheduleService scheduleService;

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLessonByIdEntity(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Lesson with id: '" + id + "' not found"));
    }

    @Override
    public LessonDto getLessonById(Long id) {
        return lessonMapper.toLessonDto(getLessonByIdEntity(id));
    }

    @Override
    public LessonDto createLesson(LessonDtoRequest lessonDtoRequest) {
        return lessonMapper.toLessonDto(save(lessonMapper.toLessonEntity(lessonDtoRequest)));
    }

    @Override
    public LessonDto updateLesson(LessonDtoRequest lessonDtoRequest) {
        Lesson request = lessonMapper.toLessonEntity(lessonDtoRequest);
        Lesson lesson = getLessonByIdEntity(lessonDtoRequest.getId());

        if (request.getCreationDate() == null)
            request.setCreationDate(lesson.getCreationDate());

        if (request.getHomework() == null)
            request.setHomework(lesson.getHomework());

        lesson = lesson.toBuilder()
                .topic(request.getTopic())
                .homework(request.getHomework())
                .creationDate(request.getCreationDate())
                .schedule(request.getSchedule())
                .build();

        return lessonMapper.toLessonDto(save(lesson));
    }

    @Override
    public List<LessonDto> getAllLesson() {
        return lessonMapper.toLessonDtoList(lessonRepository.findAll());
    }

    @Override
    public List<LessonDto> getAllLessonByTeacherId(Long teacherId) {
        return lessonMapper.toLessonDtoList(lessonRepository.getAllLessonsByTeacherId(teacherId));
    }

    @Override
    public List<LessonDto> getAllLessonByGradeId(Long gradeId) {
        return lessonMapper.toLessonDtoList(lessonRepository.getAllLessonsByGradeId(gradeId));
    }

    @Override
    public List<LessonDto> getAllLessonBySubjectId(Long subjectId) {
        return lessonMapper.toLessonDtoList(lessonRepository.getAllLessonsBySubjectId(subjectId));
    }

    @Override
    public List<LessonDto> getAllLessonByYear(String year) {
        return lessonMapper.toLessonDtoList(lessonRepository.getAllLessonsByYear(year));
    }

    @Override
    public List<LessonDto> getAllLessonByQuarter(Integer quarter) {
        return lessonMapper.toLessonDtoList(lessonRepository.getAllLessonsByQuarter(quarter));
    }

    @Override
    public List<LessonDto> getAllLessonBySubjectQuarter(Long subjectId, Long gradeId) {
        return lessonMapper.toLessonDtoList(lessonRepository.getAllLessonBySubjectQuarter(subjectId, gradeId));
    }
}
