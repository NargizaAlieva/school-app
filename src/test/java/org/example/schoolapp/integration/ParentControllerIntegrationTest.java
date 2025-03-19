package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schoolapp.controller.ParentController;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.service.ParentService;
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

@WebMvcTest(ParentController.class)
public class ParentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParentService parentService;

    @Autowired
    private ObjectMapper objectMapper;

    private ParentDto parentDto;
    private ParentDtoRequest parentDtoRequest;

    @BeforeEach
    void setUp() {
        parentDto = new ParentDto();
        parentDto.setId(1L);
        parentDto.setUser(new UserDto());
        parentDto.setChildrenNameList(Collections.singletonList("Child Name"));

        parentDtoRequest = new ParentDtoRequest();
        parentDtoRequest.setId(1L);
        parentDtoRequest.setUserId(1L);
    }

    @Test
    void getParentById_ShouldReturnParent() throws Exception {
        Mockito.when(parentService.getDtoById(anyLong())).thenReturn(parentDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/parent/get-parent-by-id/{parentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved Parent with Id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.childrenNameList[0]").value("Child Name"));
    }

    @Test
    void getAllParents_ShouldReturnListOfParents() throws Exception {
        List<ParentDto> parents = Collections.singletonList(parentDto);
        Mockito.when(parentService.getAllParent()).thenReturn(parents);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/parent/get-all-parent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all Parents."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].childrenNameList[0]").value("Child Name"));
    }

    @Test
    void getAllActiveParents_ShouldReturnListOfActiveParents() throws Exception {
        List<ParentDto> parents = Collections.singletonList(parentDto);
        Mockito.when(parentService.getAllActiveParent()).thenReturn(parents);

        mockMvc.perform(MockMvcRequestBuilders.get("/ap1/v1/parent/get-all-active-parent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved all active Parents."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].childrenNameList[0]").value("Child Name"));
    }

    @Test
    void createParent_ShouldReturnCreatedParent() throws Exception {
        Mockito.when(parentService.createParent(any(ParentDtoRequest.class))).thenReturn(parentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/ap1/v1/parent/create-parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parentDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created Parent."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.childrenNameList[0]").value("Child Name"));
    }

    @Test
    void updateParent_ShouldReturnUpdatedParent() throws Exception {
        Mockito.when(parentService.updateParent(any(ParentDtoRequest.class))).thenReturn(parentDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/parent/update-parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parentDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated Parent with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.childrenNameList[0]").value("Child Name"));
    }

    @Test
    void deleteParent_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(parentService).deleteParent(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/ap1/v1/parent/delete-parent/{parentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted Parent with id: 1"));
    }

    @Test
    void restoreParent_ShouldReturnRestoredParent() throws Exception {
        Mockito.when(parentService.restoreParent(anyLong())).thenReturn(parentDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/ap1/v1/parent/restore-parent/{parentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully restored Parent with id: 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.childrenNameList[0]").value("Child Name"));
    }
}