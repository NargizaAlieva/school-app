package org.example.schoolapp.service.role;

import org.example.schoolapp.dto.response.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StudentRoleService {
    List<MarkDto> getAllMark();

    Double getAvgMarkBySubjectGradeStudent(Long subjectId);

    Double getAvgMarkByGradeStudent();

    List<ScheduleDto> getStudentSchedule();
    List<LessonDto> getAllLessonByGrade();

    List<StudentDto> getClassmates();
}
