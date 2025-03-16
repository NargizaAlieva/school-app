package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.service.ParentService;
import org.example.schoolapp.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    private final UserMapper userMapper;
    private final UserService userService;
    private final GradeService gradeService;
    private final ParentService parentService;

    public StudentDto toStudentDto(Student student) {
        return new StudentDto().toBuilder()
                .id(student.getId())
                .birthday(student.getBirthday())
                .gradeTitle(student.getGrade().getTitle())
                .parentStatus(student.getParentStatus())
                .parentId(student.getParent().getId())
                .user(userMapper.toUserDto(student.getUser()))
                .build();
    }

    public List<StudentDto> toStudentDtoList(List<Student> students) {
        return students.stream().map(this::toStudentDto).collect(Collectors.toList());
    }

    public Student toStudentEntity (StudentDtoRequest studentDtoRequest) {
        return new Student().toBuilder()
                .id(studentDtoRequest.getId())
                .birthday(studentDtoRequest.getBirthday())
                .parentStatus(stringToParentStatus(studentDtoRequest))
                .user(userService.getEntityById(studentDtoRequest.getUserId()))
                .grade(gradeService.getByIdEntity(studentDtoRequest.getGradeId()))
                .parent(parentService.getByIdEntity(studentDtoRequest.getParentId()))
                .build();
    }

    private ParentStatus stringToParentStatus(StudentDtoRequest studentDtoRequest) {
        for (ParentStatus d : ParentStatus.values()) {
            if (studentDtoRequest.getParentStatus().toUpperCase().equals(d.name()))
                return d;
        }

        return null;
    }
}
