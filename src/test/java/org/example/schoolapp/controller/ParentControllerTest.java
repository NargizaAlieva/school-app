package org.example.schoolapp.controller;

import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.ParentService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ParentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ParentService parentService;

    @InjectMocks
    private ParentController parentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(parentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getParentById() throws Exception {
        ParentDto parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(List.of("Child1", "Child2"));

        when(parentService.getDtoById(anyLong())).thenReturn(parentDto);

        mockMvc.perform(get("/ap1/v1/parent/get-parent-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Parent with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.childrenNameList[0]").value("Child1"))
                .andExpect(jsonPath("$.data.childrenNameList[1]").value("Child2"));
    }

    @Test
    void getAllParents() throws Exception {
        ParentDto parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(List.of("Child1", "Child2"));

        when(parentService.getAllParent()).thenReturn(List.of(parentDto));

        mockMvc.perform(get("/ap1/v1/parent/get-all-parent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Parents."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].childrenNameList[0]").value("Child1"))
                .andExpect(jsonPath("$.data[0].childrenNameList[1]").value("Child2"));
    }

    @Test
    void getAllParents_EmptyList() throws Exception {
        when(parentService.getAllParent()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/parent/get-all-parent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Parent found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void getAllActiveParents() throws Exception {
        ParentDto parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(List.of("Child1", "Child2"));

        when(parentService.getAllActiveParent()).thenReturn(List.of(parentDto));

        mockMvc.perform(get("/ap1/v1/parent/get-all-active-parent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all active Parents."))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].childrenNameList[0]").value("Child1"))
                .andExpect(jsonPath("$.data[0].childrenNameList[1]").value("Child2"));
    }

    @Test
    void getAllActiveParents_EmptyList() throws Exception {
        when(parentService.getAllActiveParent()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ap1/v1/parent/get-all-active-parent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No active Parent found."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void createParent() throws Exception {
        ParentDto parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(List.of("Child1", "Child2"));

        when(parentService.createParent(any(ParentDtoRequest.class))).thenReturn(parentDto);

        mockMvc.perform(post("/ap1/v1/parent/create-parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Parent."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.childrenNameList[0]").value("Child1"))
                .andExpect(jsonPath("$.data.childrenNameList[1]").value("Child2"));
    }

    @Test
    void updateParent() throws Exception {
        ParentDto parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(List.of("Child1", "Child2"));

        when(parentService.updateParent(any(ParentDtoRequest.class))).thenReturn(parentDto);

        mockMvc.perform(put("/ap1/v1/parent/update-parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"userId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Parent with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.childrenNameList[0]").value("Child1"))
                .andExpect(jsonPath("$.data.childrenNameList[1]").value("Child2"));
    }

    @Test
    void deleteParent() throws Exception {
        mockMvc.perform(delete("/ap1/v1/parent/delete-parent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Parent with id: 1"));
    }

    @Test
    void restoreParent() throws Exception {
        ParentDto parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(List.of("Child1", "Child2"));

        when(parentService.restoreParent(anyLong())).thenReturn(parentDto);

        mockMvc.perform(put("/ap1/v1/parent/restore-parent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Parent with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.childrenNameList[0]").value("Child1"))
                .andExpect(jsonPath("$.data.childrenNameList[1]").value("Child2"));
    }
}