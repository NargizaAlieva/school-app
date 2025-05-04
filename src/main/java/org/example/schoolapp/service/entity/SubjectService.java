package org.example.schoolapp.service.entity;

import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.entity.Subject;

import java.util.List;

public interface SubjectService {
    Subject save(Subject subject);
    Subject findByIdEntity(Long id);
    SubjectDto getDtoById(Long id);

    SubjectDto getByTitle(String title);

    List<SubjectDto> getAllSubject();
    List<SubjectDto> getAllActiveSubject();

    SubjectDto createSubject(SubjectDtoRequest subjectDtoRequest);
    SubjectDto updateSubject(SubjectDtoRequest subjectDtoRequest);

    void deleteSubject(Long id);
    SubjectDto restoreSubject(Long id);
}
