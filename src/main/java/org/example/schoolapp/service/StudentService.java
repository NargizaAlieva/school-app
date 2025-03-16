package org.example.schoolapp.service;

import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.entity.Student;

import java.util.List;

public interface StudentService {
    Student save(Student student);
    Student getStudentByIdEntity(Long id);
    StudentDto getStudentById(Long id);
    Student getStudentByUserId(Long id);

    StudentDto createStudent(StudentDtoRequest studentDtoRequest);
    StudentDto updateStudent(StudentDtoRequest studentDtoRequest);

    StudentDto deleteStudent(Long id);
    StudentDto restoreStudent(Long id);

    List<StudentDto> getAllStudent();
    List<StudentDto> getAllActiveStudent();

    List<StudentDto> getStudentByParentId(Long parentId);

    List<StudentDto> getAllStudentByGrade(Long id);
    List<StudentDto> getAllStudentByGrades(List<Long> gradeId);
}
