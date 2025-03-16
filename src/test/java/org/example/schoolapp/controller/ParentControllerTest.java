package org.example.schoolapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.ParentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ParentControllerTest {

    @Mock
    private ParentService parentService;

    @InjectMocks
    private ParentController parentController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ParentDto getSampleParent() {
        return ParentDto.builder()
                .id(1L)
                .user(new UserDto())
                .build();
    }

    private ParentDtoRequest getSampleParentRequest() {
        return ParentDtoRequest.builder()
                .userId(1L)
                .build();
    }

    @Test
    void testGetParentById() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();

        when(parentService.getDtoById(1L)).thenReturn(getSampleParent());

        mockMvc.perform(get("/ap1/v1/parent/get-parent-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successfully retrieved Parent with Id: 1")))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    void testGetAllParents() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();

        when(parentService.getAllParent()).thenReturn(List.of(getSampleParent()));

        mockMvc.perform(get("/ap1/v1/parent/get-all-parent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)));
    }

    @Test
    void testCreateParent() throws Exception {
        ParentDtoRequest parentDtoRequest = getSampleParentRequest();
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();

        when(parentService.createParent(any())).thenReturn(getSampleParent());

        mockMvc.perform(post("/ap1/v1/parent/create-parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parentDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Successfully created Parent.")))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    void testUpdateParent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();

        when(parentService.updateParent(any())).thenReturn(getSampleParent());

        mockMvc.perform(put("/ap1/v1/parent/update-parent")
                    .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSampleParent())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Successfully updated Parent")))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    void testDeleteParent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();

        doNothing().when(parentService).deleteParent(1L);

        mockMvc.perform(delete("/ap1/v1/parent/delete-parent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successfully deleted Parent with id: 1")));
    }

    @Test
    void testRestoreParent() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();

        when(parentService.restoreParent(1L)).thenReturn(getSampleParent());

        mockMvc.perform(put("/ap1/v1/parent/restore-parent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successfully restored Parent with id: 1")))
                .andExpect(jsonPath("$.data.id", is(1)));
    }
}