package org.example.schoolapp.service.impl;

import lombok.AllArgsConstructor;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.response.UserDto;
import org.example.schoolapp.util.exception.*;
import org.example.schoolapp.util.mapper.UserMapper;
import org.springframework.stereotype.Service;

import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.RoleService;
import org.example.schoolapp.service.UserService;

import java.util.Set;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public Boolean isUsernameExist(String username) {
        if (userRepository.existsByUsername(username))
            throw new AlreadyExistException("User", "username", username);
        return false;
    }

    public Boolean isEmailExist(String email) {
        if (userRepository.existsByEmail(email))
            throw new AlreadyExistException("User", "email", email);
        return false;
    }

    @Override
    public User getEntityById(Long id) {
        if (id == null) {
            throw new IncorrectRequestException("User id");
        }
        return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User with id: '" + id + "' not found"));
    }

    public User getByUsernameEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User with username: '" + username + "' not found"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toUserDto(getEntityById(id));
    }

    @Override
    public UserDto getByUsername(String username) {
        return userMapper.toUserDto(getByUsernameEntity(username));
    }

    @Override
    public UserDto getByEmail(String mail) {
        User user = userRepository.findByEmail(mail)
                .orElseThrow(() -> new ObjectNotFoundException("User with mail: '" + mail + "' not found"));
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUser() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    @Override
    public List<UserDto> getAllActiveUser() {
        return userMapper.toUserDtoList(userRepository.findByIsActiveTrue());
    }

    @Override
    public List<User> getUserListWithRole(String role) {
        return userRepository.findUsersByRole(role);
    }

    @Override
    public List<UserDto> getUserDtoListWithRole(String role) {
        return userMapper.toUserDtoList(getUserListWithRole(role));
    }

    @Override
    public UserDto saveUser(User user) {
        return userMapper.toUserDto(save(user));
    }

    @Override
    public UserDto createUser(UserDtoRequest userDtoRequest) {
        if (!(isUsernameExist(userDtoRequest.getUsername())
                || isEmailExist(userDtoRequest.getEmail())))
            return saveUser(userMapper.toUserEntity(userDtoRequest));
        return null;
    }

    @Override
    public UserDto updateUser(UserDtoRequest userDtoRequest)  {
        User request = userMapper.toUserEntity(userDtoRequest);
        User user = getEntityById(userDtoRequest.getId());

        if (!user.getUsername().equals(request.getUsername()))
            isUsernameExist(request.getUsername());

        if (!user.getEmail().equals(request.getEmail()))
            isEmailExist(request.getEmail());

        if (request.getIsActive() == null)
            request.setIsActive(user.getIsActive());

        if (request.getCreationDate() == null)
            request.setCreationDate(user.getCreationDate());

        user = user.toBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .isActive(request.getIsActive())
                .creationDate(request.getCreationDate())
                .build();

        return userMapper.toUserDto(save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = getEntityById(id);

        if (!user.getIsActive())
            throw new AlreadyDisabledException("User", id);

        user.setIsActive(false);
        save(user);
    }

    @Override
    public UserDto restoreUser(Long id) {
        User user = getEntityById(id);

        if (user.getIsActive())
            throw new AlreadyEnabledException("User", id);

        user.setIsActive(true);
        return userMapper.toUserDto(save(user));
    }

    @Override
    public UserDto addRoleToUser(UserRoleDto userRoleDto) {
        User user = getEntityById(userRoleDto.getUserId());

        Set<Role> roleSet = user.getRoleSet();
        Set<Long> roleIdSet = userRoleDto.getRoleIdSet();

        for (Long r : roleIdSet) {
            Role role = roleService.findById(r);
            roleService.addUser(role, user);
            roleSet.add(role);
        }

        user.setRoleSet(roleSet);
        return saveUser(user);
    }

    @Override
    public UserDto removeRoleFromUser(UserRoleDto userRoleDto) {
        User user = getEntityById(userRoleDto.getUserId());

        Set<Role> roleSet = user.getRoleSet();
        Set<Long> roleIdSet = userRoleDto.getRoleIdSet();

        for (Long r : roleIdSet) {
            Role role = roleService.findById(r);
            roleService.removeUser(role, user);
            roleSet.remove(roleService.findById(r));
        }
        user.setRoleSet(roleSet);
        return saveUser(user);
    }
}
