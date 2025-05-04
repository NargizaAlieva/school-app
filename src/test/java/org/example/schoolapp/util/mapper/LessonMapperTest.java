package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.service.entity.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        Subject subject = Subject.builder()
                .id(1L)
                .title("Math")
                .description("Algebra and Geometry")
                .isActive(true)
                .build();

        User user = User.builder()
                .firstName("Sara")
                .lastName("Doe")
                .middleName("M.")
                .email("sara@gmail.com")
                .build();

        Employee teacher = Employee.builder()
                .id(1L)
                .user(user)
                .build();

        Grade grade = Grade.builder()
                .id(1L)
                .title("10A")
                .classTeacher(teacher)
                .studentSet(Set.of())
                .isActive(true)
                .build();

        schedule = Schedule.builder()
                .id(1L)
                .dayOfWeek(DaysOfWeek.MONDAY)
                .dueTime("10:30-11.15")
                .quarter(1)
                .schoolYear("2023-2024")
                .subjectSchedule(subject)
                .gradeSchedule(grade)
                .teacherSchedule(teacher)
                .isApprove(true)
                .isActive(true)
                .build();

        lesson = Lesson.builder()
                .id(1L)
                .topic("Math")
                .homework("Solve equations")
                .schedule(schedule)
                .build();

        lessonDtoRequest = LessonDtoRequest.builder()
                .id(1L)
                .topic("Math")
                .homework("Solve equations")
                .scheduleId(1L)
                .build();
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

        List<LessonDto> lessonDto = lessonMapper.toLessonDtoList(lessons);

        assertNotNull(lessonDto);
        assertEquals(1, lessonDto.size());
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