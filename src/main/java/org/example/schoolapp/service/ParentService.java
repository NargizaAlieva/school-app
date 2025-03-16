package org.example.schoolapp.service;

import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.entity.Parent;

import java.util.List;

public interface ParentService {
    Parent save(Parent parent);

    Parent getParentByUserId(Long id);
    Parent getByIdEntity(Long id);

    ParentDto getDtoById(Long id);

    List<ParentDto> getAllParent();
    List<ParentDto> getAllActiveParent();

    ParentDto getParentByStudentId(Long id);

    ParentDto createParent(ParentDtoRequest parentDtoRequest);
    ParentDto updateParent(ParentDtoRequest parentDtoRequest);

    void deleteParent(Long id);
    ParentDto restoreParent(Long id);
}
