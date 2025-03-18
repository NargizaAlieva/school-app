package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        user = User.builder()
                .id(1L)
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        parent = Parent.builder()
                .id(2L)
                .user(user)
                .build();

        Subject subject = Subject.builder()
                .id(1L)
                .title("Math")
                .description("Algebra and Geometry")
                .build();

        User teacherUser = User.builder()
                .id(1L)
                .username("mara")
                .firstName("Mara")
                .lastName("Ave")
                .email("mara.ave@example.com")
                .build();

        Employee teacher = Employee.builder()
                .id(10L)
                .position("Teacher")
                .salary(5000)
                .user(teacherUser)
                .subjectSet(Set.of(subject))
                .build();

        Grade grade = Grade.builder()
                .id(1L)
                .classTeacher(teacher)
                .title("10B")
                .build();

        User childUser = User.builder()
                .id(1L)
                .username("sara")
                .firstName("Sara")
                .lastName("Doe")
                .email("sara.doe@example.com")
                .build();

        Student child = Student.builder()
                .id(10L)
                .parent(parent)
                .user(childUser)
                .grade(grade)
                .parentStatus(ParentStatus.FATHER)
                .build();

        parent.setChildrenList(Collections.singletonList(child));
    }

    @Test
    void testToParentDto() {
        ParentDto parentDto = parentMapper.toParentDto(parent);

        assertNotNull(parentDto);
        assertEquals(2L, parentDto.getId());
        assertEquals(1, parentDto.getChildrenNameList().size());
        assertEquals("Sara Doe", parentDto.getChildrenNameList().get(0));
    }

    @Test
    void testToParentDtoList() {
        List<ParentDto> parentDto = parentMapper.toParentDtoList(Collections.singletonList(parent));

        assertNotNull(parentDto);
        assertEquals(1, parentDto.size());
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
