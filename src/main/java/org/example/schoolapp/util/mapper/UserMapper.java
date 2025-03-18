package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleService roleService;

    public UserDto toUserDto(User user) {
        return new UserDto().toBuilder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())
                .creationDate(user.getCreationDate())
                .roleSet(rolesToString(user))
                .build();
    }

    public List<UserDto> toUserDtoList(List<User> tasks) {
        return tasks.stream().map(this::toUserDto).collect(Collectors.toList());
    }

    public User toUserEntity(UserDtoRequest userDtoRequest) {
        return new User().toBuilder()
                .id(userDtoRequest.getId())
                .username(userDtoRequest.getUsername())
                .firstName(userDtoRequest.getFirstName())
                .lastName(userDtoRequest.getLastName())
                .middleName(userDtoRequest.getMiddleName())
                .email(userDtoRequest.getEmail())
                .password(userDtoRequest.getPassword())
                .phone(userDtoRequest.getPhone())
                .creationDate(userDtoRequest.getCreationDate())
                .roleSet(idToRole(userDtoRequest))
                .isActive(userDtoRequest.getIsActive())
                .build();
    }

    private Set<String> rolesToString(User user) {
        Set<String> roleTitleSet = new HashSet<>();

        for (Role r : user.getRoleSet())
            roleTitleSet.add(r.getTitle());

        return roleTitleSet;
    }

    private Set<Role> idToRole(UserDtoRequest userDtoRequest) {
        Set<Role> roleSet = new HashSet<>();

        if (userDtoRequest.getRoleSet() != null)
            for (Long r : userDtoRequest.getRoleSet())
                roleSet.add(roleService.findById(r));


        return roleSet;
    }
}
