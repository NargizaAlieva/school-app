package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserMapper userMapper;

    private User user;
    private UserDtoRequest userDtoRequest;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .title("ADMIN")
                .build();

        user = User.builder()
                .id(1L)
                .username("john-doe")
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .email("john@example.com")
                .phone("+1234567890")
                .isActive(true)
                .creationDate(LocalDateTime.now())
                .roleSet(Set.of(role))
                .build();

        userDtoRequest = UserDtoRequest.builder()
                .id(1L)
                .username("john-doe")
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .email("john@example.com")
                .phone("+1234567890")
                .password("password123")
                .isActive(true)
                .creationDate(LocalDateTime.now())
                .roleSet(Set.of(1L))
                .build();
    }

    @Test
    void testToUserDto() {
        UserDto userDto = userMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getMiddleName(), userDto.getMiddleName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPhone(), userDto.getPhone());
        assertEquals(user.getIsActive(), userDto.getIsActive());
        assertEquals(user.getCreationDate(), userDto.getCreationDate());
        assertEquals(Set.of("ADMIN"), userDto.getRoleSet());
    }

    @Test
    void testToUserDtoList() {
        List<UserDto> userDtoList = userMapper.toUserDtoList(List.of(user));

        assertNotNull(userDtoList);
        assertEquals(1, userDtoList.size());
        assertEquals(user.getUsername(), userDtoList.get(0).getUsername());
    }

    @Test
    void testToUserEntity() {
        when(roleService.findById(1L)).thenReturn(role);

        User mappedUser = userMapper.toUserEntity(userDtoRequest);

        assertNotNull(mappedUser);
        assertEquals(userDtoRequest.getId(), mappedUser.getId());
        assertEquals(userDtoRequest.getUsername(), mappedUser.getUsername());
        assertEquals(userDtoRequest.getFirstName(), mappedUser.getFirstName());
        assertEquals(userDtoRequest.getLastName(), mappedUser.getLastName());
        assertEquals(userDtoRequest.getMiddleName(), mappedUser.getMiddleName());
        assertEquals(userDtoRequest.getEmail(), mappedUser.getEmail());
        assertEquals(userDtoRequest.getPhone(), mappedUser.getPhone());
        assertEquals(userDtoRequest.getIsActive(), mappedUser.getIsActive());
        assertEquals(1, mappedUser.getRoleSet().size());
        assertTrue(mappedUser.getRoleSet().contains(role));
    }
}

