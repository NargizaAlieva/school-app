package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.SubjectController;
import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.service.SubjectService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubjectController.class)
public class SubjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    private SubjectDto subjectDto;
    private SubjectDtoRequest subjectDtoRequest;

    @BeforeEach
    void setUp() {
        subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setTitle("Mathematics");

        subjectDtoRequest = new SubjectDtoRequest();
        subjectDtoRequest.setTitle("Mathematics");
    }

    @Test
    void getSubjectById_ShouldReturnSubject() throws Exception {
        Mockito.when(subjectService.getDtoById(anyLong())).thenReturn(subjectDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/subject/get-subject-by-id/{subjectId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved Subject with Id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void getSubjectByTitle_ShouldReturnSubject() throws Exception {
        Mockito.when(subjectService.getByTitle(anyString())).thenReturn(subjectDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/subject/get-subject-by-title/{title}", "Mathematics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved Subject with title: Mathematics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void getAllSubjects_ShouldReturnListOfSubjects() throws Exception {
        Mockito.when(subjectService.getAllSubject()).thenReturn(Collections.singletonList(subjectDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/subject/get-all-subject")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully got all Subjects."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Mathematics"));
    }

    @Test
    void createSubject_ShouldReturnCreatedSubject() throws Exception {
        Mockito.when(subjectService.createSubject(any(SubjectDtoRequest.class))).thenReturn(subjectDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/subject/create-subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created Subject."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void updateSubject_ShouldReturnUpdatedSubject() throws Exception {
        Mockito.when(subjectService.updateSubject(any(SubjectDtoRequest.class))).thenReturn(subjectDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/subject/update-subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated Subject with id: null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void deleteSubject_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(subjectService).deleteSubject(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/subject/delete-subject/{subjectId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted Subject with id: 1"));
    }

    @Test
    void restoreSubject_ShouldReturnRestoredSubject() throws Exception {
        Mockito.when(subjectService.restoreSubject(anyLong())).thenReturn(subjectDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/subject/restore-subject/{subjectId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully restored Subject with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Mathematics"));
    }
}