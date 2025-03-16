package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.entity.Lesson;
import org.example.schoolapp.entity.Schedule;
import org.example.schoolapp.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonMapperTest {
    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private LessonMapper lessonMapper;

    private Lesson lesson;
    private LessonDtoRequest lessonDtoRequest;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new Schedule();
        schedule.setId(1L);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTopic("Math");
        lesson.setHomework("Solve equations");
        lesson.setCreationDate(LocalDateTime.of(2024, 3, 13, 10, 0));
        lesson.setSchedule(schedule);

        lessonDtoRequest = new LessonDtoRequest();
        lessonDtoRequest.setId(1L);
        lessonDtoRequest.setTopic("Math");
        lessonDtoRequest.setHomework("Solve equations");
        lessonDtoRequest.setCreationDate(LocalDateTime.of(2024, 3, 13, 10, 0));
        lessonDtoRequest.setScheduleId(1L);
    }

    @Test
    void testToLessonDto() {
        when(scheduleMapper.toScheduleDto(schedule)).thenReturn(null);

        LessonDto lessonDto = lessonMapper.toLessonDto(lesson);

        assertNotNull(lessonDto);
        assertEquals(lesson.getId(), lessonDto.getId());
        assertEquals(lesson.getTopic(), lessonDto.getTopic());
        assertEquals(lesson.getHomework(), lessonDto.getHomework());
        assertEquals(lesson.getCreationDate(), lessonDto.getCreationDate());
    }

    @Test
    void testToLessonDtoList() {
        List<Lesson> lessons = Collections.singletonList(lesson);
        when(scheduleMapper.toScheduleDto(schedule)).thenReturn(null);

        List<LessonDto> lessonDtos = lessonMapper.toLessonDtoList(lessons);

        assertNotNull(lessonDtos);
        assertEquals(1, lessonDtos.size());
    }

    @Test
    void testToLessonEntity() {
        when(scheduleService.getScheduleByIdEntity(lessonDtoRequest.getScheduleId())).thenReturn(schedule);

        Lesson mappedLesson = lessonMapper.toLessonEntity(lessonDtoRequest);

        assertNotNull(mappedLesson);
        assertEquals(lessonDtoRequest.getId(), mappedLesson.getId());
        assertEquals(lessonDtoRequest.getTopic(), mappedLesson.getTopic());
        assertEquals(lessonDtoRequest.getHomework(), mappedLesson.getHomework());
        assertEquals(lessonDtoRequest.getCreationDate(), mappedLesson.getCreationDate());
        assertEquals(schedule, mappedLesson.getSchedule());
    }
}