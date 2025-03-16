package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentMapperTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ParentMapper parentMapper;

    private Parent parent;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        parent = new Parent();
        parent.setId(2L);
        parent.setUser(user);

        Student child = new Student();
        User childUser = new User();
        childUser.setFirstName("Jane");
        childUser.setLastName("Doe");
        child.setUser(childUser);
        parent.setChildrenList(Collections.singletonList(child));
    }

    @Test
    void testToParentDto() {
        ParentDto parentDto = parentMapper.toParentDto(parent);

        assertNotNull(parentDto);
        assertEquals(2L, parentDto.getId());
        assertEquals(1, parentDto.getChildrenNameList().size());
        assertEquals("Jane Doe", parentDto.getChildrenNameList().get(0));
    }

    @Test
    void testToParentDtoList() {
        List<ParentDto> parentDtos = parentMapper.toParentDtoList(Arrays.asList(parent));

        assertNotNull(parentDtos);
        assertEquals(1, parentDtos.size());
    }

    @Test
    void testToParentEntity() {
        ParentDtoRequest request = new ParentDtoRequest();
        request.setId(2L);
        request.setUserId(1L);

        when(userService.getEntityById(1L)).thenReturn(user);

        Parent mappedParent = parentMapper.toParentEntity(request);

        assertNotNull(mappedParent);
        assertEquals(2L, mappedParent.getId());
        assertEquals(1L, mappedParent.getUser().getId());
    }
}
