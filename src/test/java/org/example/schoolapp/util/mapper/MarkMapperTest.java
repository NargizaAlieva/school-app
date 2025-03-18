package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.service.LessonService;
import org.example.schoolapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarkMapperTest {

    @Mock
    private LessonService lessonService;

    @Mock
    private StudentService studentService;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private MarkMapper markMapper;

    private Mark mark;
    private MarkDtoRequest markDtoRequest;
    private Student student;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Subject subject = Subject.builder()
                .id(1L)
                .title("Math")
                .description("Algebra and Geometry")
                .isActive(true)
                .build();

        User user = User.builder()
                .username("Sara Doe")
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

        Schedule schedule = Schedule.builder()
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

        User parentUser = User.builder()
                .id(1L)
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        Parent parent = Parent.builder()
                .id(1L)
                .user(parentUser)
                .build();

        User studentUser = User.builder()
                .id(1L)
                .username("sara")
                .firstName("Sara")
                .lastName("Doe")
                .email("sara.doe@example.com")
                .build();

        student = Student.builder()
                .id(10L)
                .parent(parent)
                .user(studentUser)
                .grade(grade)
                .parentStatus(ParentStatus.FATHER)
                .build();

        lesson = Lesson.builder()
                .id(1L)
                .topic("Math")
                .homework("Solve equations")
                .schedule(schedule)
                .build();

        mark = Mark.builder()
                .id(3L)
                .mark(90)
                .studentMark(student)
                .lessonMark(lesson)
                .build();

        markDtoRequest = MarkDtoRequest.builder()
                .id(3L)
                .mark(90)
                .studentId(10L)
                .lessonId(1L)
                .build();
    }

    @Test
    void toMarkDto_ShouldMapCorrectly() {
        when(lessonMapper.toLessonDto(lesson)).thenReturn(null);

        MarkDto markDto = markMapper.toMarkDto(mark);

        assertNotNull(markDto);
        assertEquals(mark.getId(), markDto.getId());
        assertEquals(mark.getMark(), markDto.getMark());
        assertEquals(mark.getStudentMark().getId(), markDto.getStudentId());
        assertEquals("Sara Doe", markDto.getStudentName());

        verify(lessonMapper, times(1)).toLessonDto(lesson);
    }

    @Test
    void toMarkEntity_ShouldMapCorrectly() {
        when(studentService.getStudentByIdEntity(10L)).thenReturn(student);
        when(lessonService.getLessonByIdEntity(1L)).thenReturn(lesson);

        Mark mappedMark = markMapper.toMarkEntity(markDtoRequest);

        assertNotNull(mappedMark);
        assertEquals(markDtoRequest.getId(), mappedMark.getId());
        assertEquals(markDtoRequest.getMark(), mappedMark.getMark());
        assertEquals(student, mappedMark.getStudentMark());
        assertEquals(lesson, mappedMark.getLessonMark());

        verify(studentService, times(1)).getStudentByIdEntity(10L);
        verify(lessonService, times(1)).getLessonByIdEntity(1L);
    }
}