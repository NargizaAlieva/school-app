package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {

    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapper();
    }

    @Test
    void testToRoleDto() {
        Role role = Role.builder()
                .id(1L)
                .title("Admin")
                .build();

        RoleDto roleDto = roleMapper.toRoleDto(role);

        assertNotNull(roleDto);
        assertEquals(1L, roleDto.getId());
        assertEquals("Admin", roleDto.getTitle());
    }

    @Test
    void testToRoleDtoList() {
        Role role1 = Role.builder().id(1L).title("Admin").build();
        Role role2 = Role.builder().id(2L).title("User").build();

        List<RoleDto> roleDtos = roleMapper.toRoleDtoList(Arrays.asList(role1, role2));

        assertNotNull(roleDtos);
        assertEquals(2, roleDtos.size());
        assertEquals("Admin", roleDtos.get(0).getTitle());
        assertEquals("User", roleDtos.get(1).getTitle());
    }

    @Test
    void testToRoleEntity() {
        RoleDto roleDto = RoleDto.builder()
                .id(1L)
                .title("admin")
                .build();

        Role role = roleMapper.toRoleEntity(roleDto);

        assertNotNull(role);
        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getTitle());
    }
}
