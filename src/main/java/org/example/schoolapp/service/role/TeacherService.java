package org.example.schoolapp.service.role;

import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.dto.request.*;

import java.util.List;
import java.util.Map;

public interface TeacherService {
    Map<String, SubjectDto> getTeacherSubjectList();

    List<ScheduleDto> getTeacherSchedule();

    LessonDto createLesson(LessonDtoRequest lessonDtoRequest);
    LessonDto updateLesson(LessonDtoRequest lessonDtoRequest);

    MarkDto createMark(MarkDtoRequest markDtoRequest);
    MarkDto updateMark(MarkDtoRequest markDtoRequest);

    List<GradeDto> getAllGrade();

    List<StudentDto> getAllStudentByGrade(Long gradeId);
}
