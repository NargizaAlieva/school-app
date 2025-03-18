package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.service.StudentService;
import org.example.schoolapp.util.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getStudentById() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.getStudentById(anyLong())).thenReturn(studentDto);

        mockMvc.perform(get("/ap1/v1/student/get-student-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Student with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data.parentId").value(1L));
    }

    @Test
    void getAllStudents() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.getAllStudent()).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/ap1/v1/student/get-all-student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Students."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].parentId").value(1L));
    }

    @Test
    void getAllStudents_EmptyList() throws Exception {
        when(studentService.getAllStudent()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/student/get-all-student"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Students found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllActiveStudents() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());
        when(studentService.getAllActiveStudent()).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/ap1/v1/student/get-all-active-student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Students."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].parentId").value(1L));
    }

    @Test
    void getAllActiveStudents_EmptyList() throws Exception {
        when(studentService.getAllActiveStudent()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/student/get-all-active-student"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No active Students found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getStudentsByParentId() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.getStudentByParentId(anyLong())).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/ap1/v1/student/get-students-by-parent-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Students."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].parentId").value(1L));
    }

    @Test
    void getStudentsByParentId_EmptyList() throws Exception {
        when(studentService.getStudentByParentId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/student/get-students-by-parent-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Students with gradeId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getStudentsByGradeId() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.getAllStudentByGrade(anyLong())).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/ap1/v1/student/get-students-by-grade-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Students."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data[0].parentId").value(1L));
    }

    @Test
    void getStudentsByGradeId_EmptyList() throws Exception {
        when(studentService.getAllStudentByGrade(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/student/get-students-by-grade-id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Students with gradeId: 1 found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.createStudent(any(StudentDtoRequest.class))).thenReturn(studentDto);

        mockMvc.perform(post("/ap1/v1/student/create-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"birthday\": \"2023-01-01\", \"parentStatus\": \"FATHER\", \"userId\": 1, \"parentId\": 1, \"gradeId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Student."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data.parentId").value(1L));
    }

    @Test
    void updateStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.updateStudent(any(StudentDtoRequest.class))).thenReturn(studentDto);

        mockMvc.perform(put("/ap1/v1/student/update-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"birthday\": \"2023-01-01\", \"parentStatus\": \"FATHER\", \"userId\": 1, \"parentId\": 1, \"gradeId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Student with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data.parentId").value(1L));
    }

    @Test
    void deleteStudent() throws Exception {
        mockMvc.perform(delete("/ap1/v1/student/delete-student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Student with id: 1"));
    }

    @Test
    void restoreStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setBirthday(new Date());
        studentDto.setParentStatus(ParentStatus.FATHER);
        studentDto.setGradeTitle("Grade 10");
        studentDto.setParentId(1L);
        studentDto.setUser(new UserDto());

        when(studentService.restoreStudent(anyLong())).thenReturn(studentDto);

        mockMvc.perform(put("/ap1/v1/student/restore-student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Student with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.gradeTitle").value("Grade 10"))
                .andExpect(jsonPath("$.data.parentId").value(1L));
    }
}