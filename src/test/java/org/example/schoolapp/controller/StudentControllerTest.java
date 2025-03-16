package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
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
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getStudentById_ShouldReturnStudent() throws Exception {
        Long studentId = 1L;
        StudentDto studentDto = StudentDto.builder()
                .id(studentId)
                .user(new UserDto())
                .build();
        when(studentService.getStudentById(studentId)).thenReturn(studentDto);

        mockMvc.perform(get("/ap1/v1/student/get-student-by-id/{studentId}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Student with Id: " + studentId))
                .andExpect(jsonPath("$.data.id").value(studentId));
    }

    @Test
    void getAllStudent_ShouldReturnList() throws Exception {
        List<StudentDto> students = List.of(StudentDto.builder()
                .id(1L)
                .user(new UserDto())
                .build());
        when(studentService.getAllStudent()).thenReturn(students);

        mockMvc.perform(get("/ap1/v1/student/get-all-student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Students."))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        StudentDtoRequest request = StudentDtoRequest.builder()
                .userId(1L)
                .build();
        StudentDto responseDto = StudentDto.builder()
                .id(1L)
                .user(new UserDto())
                .build();;
        when(studentService.createStudent(any(StudentDtoRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/ap1/v1/student/create-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Student."))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void deleteStudent_ShouldReturnSuccessMessage() throws Exception {
        Long studentId = 1L;
        mockMvc.perform(delete("/ap1/v1/student/delete-student/{studentId}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Student with id: " + studentId));
    }
}