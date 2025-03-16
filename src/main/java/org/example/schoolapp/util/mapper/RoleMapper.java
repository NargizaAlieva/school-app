package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {
    public RoleDto toRoleDto(Role role) {
        return new RoleDto().toBuilder()
                .id(role.getId())
                .title(role.getTitle())
                .build();
    }

    public List<RoleDto> toRoleDtoList(List<Role> roles) {
        return roles.stream().map(this::toRoleDto).collect(Collectors.toList());
    }

    public Role toRoleEntity(RoleDto roleDto) {
        return new Role().toBuilder()
                .id(roleDto.getId())
                .title(roleDto.getTitle().toUpperCase())
                .build();
    }
}
