package org.example.schoolapp.service.role;

import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.dto.response.StudentDto;

import java.util.List;
import java.util.Map;

public interface ParentRoleService {
    Map<String, StudentDto> getChildList();

    StudentDto createStudent(StudentDtoRequest studentDtoRequest);

    List<MarkDto> getAllMark(Long childId);
    List<ScheduleDto> getStudentSchedule(Long studentId);
    void leaveSchool(Long studentId);
}
