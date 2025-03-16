package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.repository.GradeRepository;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.util.exception.AlreadyDisabledException;
import org.example.schoolapp.util.exception.AlreadyEnabledException;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.GradeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;

    @Override
    public Grade save(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Grade getByIdEntity(Long id) {
        return gradeRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Grade with id: '" + id + "' not found"));
    }
    @Override
    public GradeDto getById(Long id) {
        return gradeMapper.toGradeDto(getByIdEntity(id));
    }

    @Override
    public List<GradeDto> getAllActiveGrade() {
        List<Grade> activeGrades = gradeRepository.findAllByIsActiveTrue();
        return gradeMapper.toGradeDtoList(activeGrades);
    }

    @Override
    public List<GradeDto> getAllGradeByTeacherId(Long teacherId) {
        return gradeMapper.toGradeDtoList(gradeRepository.findByClassTeacherId(teacherId));
    }

    @Override
    public GradeDto getGradeByStudentId(Long studentId) {
        return gradeMapper.toGradeDto(gradeRepository.getGradeByStudentId(studentId));
    }

    @Override
    public List<GradeDto> getAllGrade() {
        return gradeMapper.toGradeDtoList(gradeRepository.findAll());
    }

    @Override
    public GradeDto createGrade(GradeDtoRequest gradeDtoRequest) {
        return gradeMapper.toGradeDto(save(gradeMapper.toGradeEntity(gradeDtoRequest)));
    }
    @Override
    public GradeDto updateGrade(GradeDtoRequest gradeDtoRequest) {
        Grade request = gradeMapper.toGradeEntity(gradeDtoRequest);
        Grade grade = getByIdEntity(gradeDtoRequest.getId());

        if (request.getIsActive() == null)
            request.setIsActive(grade.getIsActive());

        if (request.getCreationDate() == null)
            request.setCreationDate(grade.getCreationDate());

        grade = grade.toBuilder()
                .title(request.getTitle())
                .creationDate(request.getCreationDate())
                .classTeacher(request.getClassTeacher())
                .isActive(request.getIsActive())
                .build();

        return gradeMapper.toGradeDto(save(grade));
    }

    @Override
    public GradeDto deleteGrade(Long id) {
        Grade grade = getByIdEntity(id);

        if (!grade.getIsActive())
            throw new AlreadyDisabledException("Grade", id);

        grade.setIsActive(false);
        return gradeMapper.toGradeDto(save(grade));
    }

    @Override
    public GradeDto restoreGrade(Long id) {
        Grade grade = getByIdEntity(id);

        if (grade.getIsActive())
            throw new AlreadyEnabledException("Grade", id);

        grade.setIsActive(true);
        return gradeMapper.toGradeDto(save(grade));
    }
}
