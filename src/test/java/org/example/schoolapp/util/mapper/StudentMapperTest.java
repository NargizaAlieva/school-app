package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.service.ParentService;
import org.example.schoolapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentMapperTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Mock
    private GradeService gradeService;

    @Mock
    private ParentService parentService;

    @InjectMocks
    private StudentMapper studentMapper;

    private Student student;
    private StudentDtoRequest studentDtoRequest;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Grade grade = new Grade();
        grade.setId(2L);
        grade.setTitle("10th Grade");

        Parent parent = new Parent();
        parent.setId(3L);

        student = new Student();
        student.setId(1L);
        student.setBirthday(new Date());
        student.setParentStatus(ParentStatus.FATHER);
        student.setUser(user);
        student.setGrade(grade);
        student.setParent(parent);

        studentDtoRequest = new StudentDtoRequest();
        studentDtoRequest.setId(1L);
        studentDtoRequest.setBirthday(new Date());
        studentDtoRequest.setParentStatus("father");
        studentDtoRequest.setUserId(1L);
        studentDtoRequest.setGradeId(2L);
        studentDtoRequest.setParentId(3L);
    }

    @Test
    void testToStudentDto() {
        StudentDto studentDto = studentMapper.toStudentDto(student);
        assertNotNull(studentDto);
        assertEquals(student.getId(), studentDto.getId());
        assertEquals(student.getParentStatus(), studentDto.getParentStatus());
        assertEquals(student.getGrade().getTitle(), studentDto.getGradeTitle());
    }

    @Test
    void testToStudentDtoList() {
        List<StudentDto> studentDtos = studentMapper.toStudentDtoList(List.of(student));
        assertNotNull(studentDtos);
        assertEquals(1, studentDtos.size());
        assertEquals(student.getId(), studentDtos.get(0).getId());
    }

    @Test
    void testToStudentEntity() {
        when(userService.getEntityById(1L)).thenReturn(student.getUser());
        when(gradeService.getByIdEntity(2L)).thenReturn(student.getGrade());
        when(parentService.getByIdEntity(3L)).thenReturn(student.getParent());

        Student result = studentMapper.toStudentEntity(studentDtoRequest);

        assertNotNull(result);
        assertEquals(studentDtoRequest.getId(), result.getId());
        assertEquals(ParentStatus.FATHER, result.getParentStatus());
        assertEquals(studentDtoRequest.getUserId(), result.getUser().getId());
        assertEquals(studentDtoRequest.getGradeId(), result.getGrade().getId());
        assertEquals(studentDtoRequest.getParentId(), result.getParent().getId());
    }
}
