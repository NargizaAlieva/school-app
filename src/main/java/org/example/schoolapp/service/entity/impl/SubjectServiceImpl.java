package org.example.schoolapp.service.entity.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.repository.SubjectRepository;
import org.example.schoolapp.service.entity.SubjectService;
import org.example.schoolapp.util.exception.*;
import org.example.schoolapp.util.mapper.SubjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public Boolean isTitleExist(String title) {
        if (subjectRepository.existsByTitle(title))
            throw new AlreadyExistException("Subject", "title", title);
        return false;
    }

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject findByIdEntity(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Subject with id: '" + id + "' not found"));
    }

    @Override
    public SubjectDto getDtoById(Long id) {
        return subjectMapper.toSubjectDto(findByIdEntity(id));
    }

    @Override
    public SubjectDto getByTitle(String title) {
        Subject subject = subjectRepository.findByTitle(title)
                .orElseThrow(() -> new ObjectNotFoundException("Role with title: '" + title + "' not found"));

        return subjectMapper.toSubjectDto(subject);
    }

    @Override
    public List<SubjectDto> getAllSubject() {
        return subjectMapper.toSubjectDtoList(subjectRepository.findAll());
    }

    @Override
    public List<SubjectDto> getAllActiveSubject() {
        List<Subject> activeSubjects = subjectRepository.findByIsActiveTrue();
        return subjectMapper.toSubjectDtoList(activeSubjects);
    }

    @Override
    public SubjectDto createSubject(SubjectDtoRequest subjectDtoRequest) {
        if (subjectDtoRequest.getTitle() == null)
            throw new IncorrectRequestException("Subject with title cannot be null");

        if(!isTitleExist(subjectDtoRequest.getTitle()))
            return subjectMapper.toSubjectDto(
                    save(subjectMapper.toSubjectEntity(subjectDtoRequest)));

        return null;
    }

    @Override
    public SubjectDto updateSubject(SubjectDtoRequest subjectDtoRequest) {
        Subject request = subjectMapper.toSubjectEntity(subjectDtoRequest);
        Subject subject = findByIdEntity(subjectDtoRequest.getId());

        if(request.getTitle() == null)
            throw new IncorrectRequestException("Subject with title cannot be null");

        if (!request.getTitle().equals(subject.getTitle()))
            isTitleExist(request.getTitle());

        if(request.getIsActive() == null)
            request.setIsActive(subject.getIsActive());

        if (request.getDescription() == null)
            request.setDescription(subject.getDescription());

        if (request.getTeachersSet() == null)
            request.setTeachersSet(subject.getTeachersSet());

        subject = subject.toBuilder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .build();

        return subjectMapper.toSubjectDto(save(subject));
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = findByIdEntity(id);

        if (!subject.getIsActive())
            throw new AlreadyDisabledException("Subject", id);

        subject.setIsActive(false);
        subjectMapper.toSubjectDto(save(subject));
    }

    @Override
    public SubjectDto restoreSubject(Long id) {
        Subject subject = findByIdEntity(id);

        if (subject.getIsActive())
            throw new AlreadyEnabledException("Subject", id);

        subject.setIsActive(true);
        return subjectMapper.toSubjectDto(save(subject));
    }
}
