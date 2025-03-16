package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.service.SubjectService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    void getSubjectById_ShouldReturnSubject() throws Exception {
        Long subjectId = 1L;
        SubjectDto subjectDto = SubjectDto.builder()
                .id(subjectId)
                .title("Mathematics")
                .isActive(true)
                .build();

        when(subjectService.getDtoById(subjectId)).thenReturn(subjectDto);

        mockMvc.perform(get("/ap1/v1/subject/get-subject-by-id/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Subject with Id: " + subjectId))
                .andExpect(jsonPath("$.data.id").value(subjectId))
                .andExpect(jsonPath("$.data.title").value("Mathematics"));
    }

    @Test
    void getSubjectByTitle_ShouldReturnSubject() throws Exception {
        String title = "Physics";
        SubjectDto subjectDto = SubjectDto.builder()
                .id(2L)
                .title(title)
                .isActive(true)
                .build();

        when(subjectService.getByTitle(title)).thenReturn(subjectDto);

        mockMvc.perform(get("/ap1/v1/subject/get-subject-by-title/" + title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Subject with title: " + title))
                .andExpect(jsonPath("$.data.title").value(title));
    }

    @Test
    void getAllSubjects_ShouldReturnListOfSubjects() throws Exception {
        List<SubjectDto> subjects = List.of(SubjectDto.builder()
                .id(1L)
                .title("Math")
                .isActive(true)
                .build());
        when(subjectService.getAllSubject()).thenReturn(subjects);

        mockMvc.perform(get("/ap1/v1/subject/get-all-subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Math"));
    }

    @Test
    void createSubject_ShouldReturnCreatedSubject() throws Exception {
        SubjectDtoRequest request = SubjectDtoRequest.builder().title("Chemistry").build();
        SubjectDto subjectDto = SubjectDto.builder()
                .id(3L)
                .title("Chemistry")
                .isActive(true)
                .build();

        when(subjectService.createSubject(any(SubjectDtoRequest.class))).thenReturn(subjectDto);

        mockMvc.perform(post("/ap1/v1/subject/create-subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Chemistry\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Chemistry"));
    }

    @Test
    void deleteSubject_ShouldReturnSuccessMessage() throws Exception {
        Long subjectId = 1L;
        mockMvc.perform(delete("/ap1/v1/subject/delete-subject/1", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Subject with id: " + subjectId));
    }
}