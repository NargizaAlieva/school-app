package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.entity.Mark;
import org.example.schoolapp.repository.MarkRepository;
import org.example.schoolapp.service.*;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.MarkMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;
    private final MarkMapper markMapper;

    @Override
    public Mark save(Mark mark) {
        return markRepository.save(mark);
    }

    @Override
    public Mark getMarkByIdEntity(Long id) {
        return markRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Mark with id: '" + id + "' not found"));
    }

    @Override
    public MarkDto getMarkById(Long id) {
        return markMapper.toMarkDto(getMarkByIdEntity(id));
    }

    @Override
    public MarkDto createMark(MarkDtoRequest markDtoRequest) {
        return markMapper.toMarkDto(save(markMapper.toMarkEntity(markDtoRequest)));
    }

    @Override
    public MarkDto updateMark(MarkDtoRequest markDtoRequest) {
        Mark request = markMapper.toMarkEntity(markDtoRequest);
        Mark mark = getMarkByIdEntity(markDtoRequest.getId());

        mark = mark.toBuilder()
                .id(request.getId())
                .mark(request.getMark())
                .studentMark(request.getStudentMark())
                .lessonMark(request.getLessonMark())
                .build();

        return markMapper.toMarkDto(save(mark));
    }

    @Override
    public List<MarkDto> getAllMark() {
        return markMapper.toMarkDtoList(markRepository.findAll());
    }

    @Override
    public List<MarkDto> getAllMarkByStudent(Long studentId) {
        return markMapper.toMarkDtoList(markRepository.findByStudentMarkId(studentId));
    }

    @Override
    public List<MarkDto> getMarksByStudentAndSchedule(Long studentId, Long subjectId, String year, Integer quarter) {
        return markMapper.toMarkDtoList(markRepository.findMarksByStudentAndSchedule(studentId, subjectId, year, quarter));
    }

    @Override
    public List<MarkDto> getMarksByStudentAndSchedule(Long studentId, String year, Integer quarter) {
        return markMapper.toMarkDtoList(markRepository.findMarksByStudentAndSchedule(studentId, year, quarter));
    }

    @Override
    public List<MarkDto> getMarksByStudentAndSchedule(Long studentId, String year) {
        return markMapper.toMarkDtoList(markRepository.findMarksByStudentAndSchedule(studentId, year));
    }

    @Override
    public Double getAverageMarkByStudentId(Long studentId) {
        return markRepository.findAverageMarkByStudentId(studentId);
    }

    @Override
    public Double getAverageMarkByStudentIdAndSubjectId(Long studentId, Long subjectId) {
        return markRepository.findAverageMarkByStudentIdAndSubjectId(studentId, subjectId);
    }
}
