package org.example.schoolapp.service.entity;

import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.entity.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User getEntityById(Long id);
    UserDto getById(Long id);
    UserDto getByUsername(String username);
    UserDto getByEmail(String mail);
    User getCurrentUser();

    List<UserDto> getAllUser();
    List<UserDto> getAllActiveUser();
    List<User> getUserListWithRole(String role);
    List<UserDto> getUserDtoListWithRole(String role);

    UserDto saveUser(User user);
    UserDto createUser(UserDtoRequest userDtoRequest);
    UserDto updateUser(UserDtoRequest userDtoRequest);

    UserDto restoreUser(Long id);
    void deleteUser(Long id);

    UserDto addRoleToUser(UserRoleDto userRoleDto);
    UserDto removeRoleFromUser(UserRoleDto userRoleDto);
}
