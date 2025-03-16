package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.repository.ParentRepository;
import org.example.schoolapp.service.ParentService;
import org.example.schoolapp.service.UserService;
import org.example.schoolapp.util.exception.AlreadyExistException;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.ParentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;
    private final UserService userService;

    public List<Parent> getAllParentEntity(){
        return parentRepository.findAll();
    }

    public Boolean isExistByUserId(Long id) {
        for (ParentDto e : getAllParent())
            if (e.getUser().getId().equals(id))
                return true;

        return false;
    }

    @Override
    public Parent save(Parent parent) {
        return parentRepository.save(parent);
    }

    @Override
    public Parent getParentByUserId(Long id) {
        for (ParentDto p : getAllParent())
            if (p.getUser().getId().equals(id))
                return getByIdEntity(p.getId());

        throw new ObjectNotFoundException("Parent with userId: '" + id + "' not found");
    }

    @Override
    public Parent getByIdEntity(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() ->  new ObjectNotFoundException("Parent with id: '" + id + "' not found"));
    }

    @Override
    public ParentDto getDtoById(Long id) {
        return parentMapper.toParentDto(getByIdEntity(id));
    }

    @Override
    public List<ParentDto> getAllParent() {
        return parentMapper.toParentDtoList(getAllParentEntity());
    }

    @Override
    public List<ParentDto> getAllActiveParent() {
        List<Parent> activeParents = parentRepository.findAllActiveParents();
        return parentMapper.toParentDtoList(activeParents);
    }

    @Override
    public ParentDto getParentByStudentId(Long id) {
        return parentMapper.toParentDto(parentRepository.getParentsByStudentId(id));
    }

    @Override
    public ParentDto createParent(ParentDtoRequest parentDtoRequest) {
        if (isExistByUserId(parentDtoRequest.getUserId()))
            throw new AlreadyExistException("Employee", "userId", parentDtoRequest.getUserId());

        return parentMapper.toParentDto(save(parentMapper.toParentEntity(parentDtoRequest)));
    }

    @Override
    public ParentDto updateParent(ParentDtoRequest parentDtoRequest) {
        if (userService.getById(parentDtoRequest.getUserId()) == null)
            throw new ObjectNotFoundException("User");

        Parent request = getByIdEntity(parentDtoRequest.getId());
        Parent parent = parentMapper.toParentEntity(parentDtoRequest);

        if(!request.getUser().getId().equals(parent.getUser().getId()))
            if (isExistByUserId(parentDtoRequest.getUserId()))
                throw new AlreadyExistException("Parent", "userId", parentDtoRequest.getUserId());

        parent = parent.toBuilder().
                id(request.getId())
                .user(request.getUser())
                .build();

        return parentMapper.toParentDto(save(parent));
    }

    @Override
    public void deleteParent(Long id) {
        Parent parent = getParentByUserId(id);
        userService.deleteUser(parent.getUser().getId());
    }

    @Override
    public ParentDto restoreParent(Long id) {
        Parent parent = getParentByUserId(id);
        userService.restoreUser(parent.getUser().getId());
        return parentMapper.toParentDto(parent);
    }
}
