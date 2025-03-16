package org.example.schoolapp.service;

import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;

import java.util.List;

public interface RoleService {
    Role save(Role role);

    Role findById(Long id);
    RoleDto getDtoById(Long id);
    List<RoleDto> getAllRole();

    RoleDto createRole(RoleDto role);
    RoleDto updateRole(RoleDto role);

    Role addUser(Role role, User user);
    Role removeUser(Role role, User user);
}
