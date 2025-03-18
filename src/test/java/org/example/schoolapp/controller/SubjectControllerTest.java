package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.service.SubjectService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getSubjectById() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Mathematics");

        when(subjectService.getDtoById(anyLong())).thenReturn(subjectDto);

        mockMvc.perform(get("/ap1/v1/subject/get-subject-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Subject with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void getSubjectByTitle() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Mathematics");

        when(subjectService.getByTitle(anyString())).thenReturn(subjectDto);

        mockMvc.perform(get("/ap1/v1/subject/get-subject-by-title/Mathematics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Subject with title: Mathematics"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void getAllSubjects() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Mathematics");

        when(subjectService.getAllSubject()).thenReturn(List.of(subjectDto));

        mockMvc.perform(get("/ap1/v1/subject/get-all-subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Subjects."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Mathematics"));
    }

    @Test
    void getAllSubjects_EmptyList() throws Exception {
        when(subjectService.getAllSubject()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/subject/get-all-subject"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No subjects found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getAllActiveSubjects() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Mathematics");
        subjectDto.setIsActive(true);

        when(subjectService.getAllActiveSubject()).thenReturn(List.of(subjectDto));

        mockMvc.perform(get("/ap1/v1/subject/get-all-active-subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Subjects."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    void getAllActiveSubjects_EmptyList() throws Exception {
        when(subjectService.getAllActiveSubject()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/subject/get-all-active-subject"))
                .andExpect(status().isNotFound()) // Expect 404 status
                .andExpect(jsonPath("$.message").value("No subjects found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createSubject() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Mathematics");

        when(subjectService.createSubject(any(SubjectDtoRequest.class))).thenReturn(subjectDto);

        mockMvc.perform(post("/ap1/v1/subject/create-subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Mathematics\", \"description\": \"Math subject\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Subject."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void updateSubject() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Updated Mathematics");

        when(subjectService.updateSubject(any(SubjectDtoRequest.class))).thenReturn(subjectDto);

        mockMvc.perform(put("/ap1/v1/subject/update-subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"Updated Mathematics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Subject with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Updated Mathematics"));
    }

    @Test
    void deleteSubject() throws Exception {
        mockMvc.perform(delete("/ap1/v1/subject/delete-subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Subject with id: 1"));
    }

    @Test
    void restoreSubject() throws Exception {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Restored Mathematics");

        when(subjectService.restoreSubject(anyLong())).thenReturn(subjectDto);

        mockMvc.perform(put("/ap1/v1/subject/restore-subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Subject with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Restored Mathematics"));
    }
}