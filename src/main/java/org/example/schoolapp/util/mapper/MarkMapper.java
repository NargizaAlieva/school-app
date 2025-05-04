package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.entity.Mark;
import org.example.schoolapp.service.entity.LessonService;
import org.example.schoolapp.service.entity.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarkMapper {
    private final LessonService lessonService;
    private final StudentService studentService;
    private final LessonMapper lessonMapper;

    public MarkDto toMarkDto (Mark mark) {
        return new MarkDto().toBuilder()
                .id(mark.getId())
                .mark(mark.getMark())
                .lesson(lessonMapper.toLessonDto(mark.getLessonMark()))
                .studentId(mark.getStudentMark().getId())
                .studentName(studentToString(mark))
                .build();
    }

    public List<MarkDto> toMarkDtoList(List<Mark> marks) {
        return marks.stream().map(this::toMarkDto).collect(Collectors.toList());
    }

    public Mark toMarkEntity(MarkDtoRequest markDtoRequest) {
        return new Mark().toBuilder()
                .id(markDtoRequest.getId())
                .mark(markDtoRequest.getMark())
                .studentMark(studentService.getStudentByIdEntity(markDtoRequest.getStudentId()))
                .lessonMark(lessonService.getLessonByIdEntity(markDtoRequest.getLessonId()))
                .build();
    }

    private String studentToString(Mark mark) {
        String studentName = mark.getStudentMark().getUser().getFirstName() + " " + mark.getStudentMark().getUser().getLastName();
        if(mark.getStudentMark().getUser().getMiddleName() != null)
            studentName += " " + mark.getStudentMark().getUser().getMiddleName();

        return studentName;
    }
}
