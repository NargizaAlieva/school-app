package org.example.schoolapp.service;

import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.entity.Subject;

import java.util.List;
import java.util.Set;

public interface SubjectService {
    Subject save(Subject subject);
    Subject findByIdEntity(Long id);
    SubjectDto getDtoById(Long id);

    SubjectDto getByTitle(String title);

    List<SubjectDto> getAllSubject();
    List<SubjectDto> getAllActiveSubject();
    Set<SubjectDto> getSubjectForGrade(Long gradeId, List<ScheduleDto> scheduleDtoList);
    List<SubjectDto> getSubjectSchedule(List<ScheduleDto> scheduleDtoList);

    SubjectDto createSubject(SubjectDtoRequest subjectDtoRequest);
    SubjectDto updateSubject(SubjectDtoRequest subjectDtoRequest);

    SubjectDto deleteSubject(Long id);
    SubjectDto restoreSubject(Long id);
}
