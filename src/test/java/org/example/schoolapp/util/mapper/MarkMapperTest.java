package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.entity.Lesson;
import org.example.schoolapp.entity.Mark;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.LessonService;
import org.example.schoolapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("M");

        student = new Student();
        student.setId(1L);
        student.setUser(user);

        lesson = new Lesson();
        lesson.setId(2L);

        mark = new Mark();
        mark.setId(3L);
        mark.setMark(90);
        mark.setStudentMark(student);
        mark.setLessonMark(lesson);

        markDtoRequest = new MarkDtoRequest();
        markDtoRequest.setId(3L);
        markDtoRequest.setMark(90);
        markDtoRequest.setStudentId(1L);
        markDtoRequest.setLessonId(2L);
    }

    @Test
    void toMarkDto_ShouldMapCorrectly() {
        when(lessonMapper.toLessonDto(lesson)).thenReturn(null);

        MarkDto markDto = markMapper.toMarkDto(mark);

        assertNotNull(markDto);
        assertEquals(mark.getId(), markDto.getId());
        assertEquals(mark.getMark(), markDto.getMark());
        assertEquals(mark.getStudentMark().getId(), markDto.getStudentId());
        assertEquals("John Doe M", markDto.getStudentName());

        verify(lessonMapper, times(1)).toLessonDto(lesson);
    }

    @Test
    void toMarkEntity_ShouldMapCorrectly() {
        when(studentService.getStudentByIdEntity(1L)).thenReturn(student);
        when(lessonService.getLessonByIdEntity(2L)).thenReturn(lesson);

        Mark mappedMark = markMapper.toMarkEntity(markDtoRequest);

        assertNotNull(mappedMark);
        assertEquals(markDtoRequest.getId(), mappedMark.getId());
        assertEquals(markDtoRequest.getMark(), mappedMark.getMark());
        assertEquals(student, mappedMark.getStudentMark());
        assertEquals(lesson, mappedMark.getLessonMark());

        verify(studentService, times(1)).getStudentByIdEntity(1L);
        verify(lessonService, times(1)).getLessonByIdEntity(2L);
    }
}