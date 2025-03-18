package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.service.RoleService;
import org.example.schoolapp.util.exception.AlreadyExistException;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto save (RoleDto roleDto){
        return roleMapper.toRoleDto(save(roleMapper.toRoleEntity(roleDto)));
    }

    @Override
    public Role save(Role role){
        return roleRepository.save(role);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Role with id: '" + id + "' not found"));
    }

    @Override
    public RoleDto getDtoById(Long id) {
        return roleMapper.toRoleDto(findById(id));
    }

    @Override
    public List<RoleDto> getAllRole() {
        return roleMapper.toRoleDtoList(roleRepository.findAll());
    }

    @Override
    public RoleDto createRole(RoleDto request) {
        if (roleRepository.existsByTitle(request.getTitle()))
            throw new AlreadyExistException("Role", "title", request.getTitle());
        return save(request);
    }

    @Override
    public RoleDto updateRole(RoleDto request) {
        Role newRole = findById(request.getId());

        newRole = newRole.toBuilder()
                        .id(request.getId())
                        .title(request.getTitle().toUpperCase())
                        .build();

        return roleMapper.toRoleDto(save(newRole));
    }

    @Override
    public void addUser(Role role, User user) {
        Set<User> roleSet = role.getUserSet();
        roleSet.add(user);
        role.setUserSet(roleSet);
        save(role);
    }

    @Override
    public void removeUser(Role role, User user) {
        Set<User> roleSet = role.getUserSet();
        roleSet.remove(user);
        role.setUserSet(roleSet);
        save(role);
    }
}
