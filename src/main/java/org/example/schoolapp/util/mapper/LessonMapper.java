package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.entity.Lesson;
import org.example.schoolapp.service.entity.ScheduleService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LessonMapper {
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    public LessonDto toLessonDto(Lesson lesson) {
        return new LessonDto().toBuilder()
                .id(lesson.getId())
                .topic(lesson.getTopic())
                .homework(lesson.getHomework())
                .creationDate(lesson.getCreationDate())
                .schedule(scheduleMapper.toScheduleDto(lesson.getSchedule()))
                .build();
    }

    public List<LessonDto> toLessonDtoList(List<Lesson> lessons) {
        return lessons.stream().map(this::toLessonDto).collect(Collectors.toList());
    }

    public Lesson toLessonEntity(LessonDtoRequest lessonDtoRequest) {
        if(lessonDtoRequest.getCreationDate() == null)
            lessonDtoRequest.setCreationDate(LocalDateTime.now());

        return new Lesson().toBuilder()
                .id(lessonDtoRequest.getId())
                .topic(lessonDtoRequest.getTopic())
                .homework(lessonDtoRequest.getHomework())
                .creationDate(lessonDtoRequest.getCreationDate())
                .schedule(scheduleService.getScheduleByIdEntity(lessonDtoRequest.getScheduleId()))
                .build();
    }
}
