package org.example.schoolapp.service;

import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.LessonDto;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.entity.Mark;

import java.util.List;

public interface MarkService {
    Mark save(Mark mark);

    Mark getMarkByIdEntity(Long id);

    MarkDto getMarkById(Long id);

    MarkDto createMark(MarkDtoRequest markDtoRequest);
    MarkDto updateMark(MarkDtoRequest markDtoRequest);

    List<MarkDto> getAllMark();
    List<MarkDto> getAllMarkByStudent(Long studentId);

    List<MarkDto> getMarksByStudentAndSchedule(Long studentId, Long subjectId, String year, Integer quarter);
    List<MarkDto> getMarksByStudentAndSchedule(Long studentId, String year, Integer quarter);
    List<MarkDto> getMarksByStudentAndSchedule(Long studentId, String year);

    Double getAverageMarkByStudentId(Long studentId);

    Double getAverageMarkByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
