package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.StudentController;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDto studentDto;
    private StudentDtoRequest studentDtoRequest;

    @BeforeEach
    void setUp() {
        studentDto = new StudentDto();
        studentDto.setId(1L);

        studentDtoRequest = new StudentDtoRequest();
    }

    @Test
    void getStudentById_ShouldReturnStudent() throws Exception {
        Mockito.when(studentService.getStudentById(anyLong())).thenReturn(studentDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/student/get-student-by-id/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved Student with Id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() throws Exception {
        List<StudentDto> students = Collections.singletonList(studentDto);
        Mockito.when(studentService.getAllStudent()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/student/get-all-student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Students."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getAllActiveStudents_ShouldReturnListOfActiveStudents() throws Exception {
        List<StudentDto> students = Collections.singletonList(studentDto);
        Mockito.when(studentService.getAllActiveStudent()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/student/get-all-active-student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all active Students."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getStudentsByParentId_ShouldReturnListOfStudents() throws Exception {
        List<StudentDto> students = Collections.singletonList(studentDto);
        Mockito.when(studentService.getStudentByParentId(anyLong())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/student/get-students-by-parent-id/{parentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all active Students."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void getStudentsByGradeId_ShouldReturnListOfStudents() throws Exception {
        List<StudentDto> students = Collections.singletonList(studentDto);
        Mockito.when(studentService.getAllStudentByGrade(anyLong())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/student/get-students-by-grade-id/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all active Students."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        Mockito.when(studentService.createStudent(any(StudentDtoRequest.class))).thenReturn(studentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/student/create-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created Student."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        Mockito.when(studentService.updateStudent(any(StudentDtoRequest.class))).thenReturn(studentDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/student/update-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated Student with id: null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void deleteStudent_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(studentService).deleteStudent(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/student/delete-student/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted Student with id: 1"));
    }

    @Test
    void restoreStudent_ShouldReturnRestoredStudent() throws Exception {
        Mockito.when(studentService.restoreStudent(anyLong())).thenReturn(studentDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/student/restore-student/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully restored Student with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }
}