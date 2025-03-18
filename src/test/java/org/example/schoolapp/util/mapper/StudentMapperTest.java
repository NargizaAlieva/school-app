package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.entity.*;
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

import java.util.List;
import java.util.Set;

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
        User user = User.builder()
                .id(1L)
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        Parent parent = Parent.builder()
                .id(1L)
                .user(user)
                .build();

        Subject subject = Subject.builder()
                .id(1L)
                .title("Math")
                .description("Algebra and Geometry")
                .build();

        User teacherUser = User.builder()
                .id(1L)
                .username("mara")
                .firstName("Mara")
                .lastName("Ave")
                .email("mara.ave@example.com")
                .build();

        Employee teacher = Employee.builder()
                .id(10L)
                .position("Teacher")
                .salary(5000)
                .user(teacherUser)
                .subjectSet(Set.of(subject))
                .build();

        Grade grade = Grade.builder()
                .id(1L)
                .classTeacher(teacher)
                .title("10B")
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

        studentDtoRequest = StudentDtoRequest.builder()
                .id(1L)
                .parentStatus("father")
                .userId(1L)
                .gradeId(1L)
                .parentId(1L)
                .build();
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
        List<StudentDto> studentDto = studentMapper.toStudentDtoList(List.of(student));
        assertNotNull(studentDto);
        assertEquals(1, studentDto.size());
        assertEquals(student.getId(), studentDto.get(0).getId());
    }

    @Test
    void testToStudentEntity() {
        when(userService.getEntityById(1L)).thenReturn(student.getUser());
        when(gradeService.getByIdEntity(1L)).thenReturn(student.getGrade());
        when(parentService.getByIdEntity(1L)).thenReturn(student.getParent());

        Student result = studentMapper.toStudentEntity(studentDtoRequest);

        assertNotNull(result);
        assertEquals(studentDtoRequest.getId(), result.getId());
        assertEquals(ParentStatus.FATHER, result.getParentStatus());
        assertEquals(studentDtoRequest.getUserId(), result.getUser().getId());
        assertEquals(studentDtoRequest.getGradeId(), result.getGrade().getId());
        assertEquals(studentDtoRequest.getParentId(), result.getParent().getId());
    }
}
