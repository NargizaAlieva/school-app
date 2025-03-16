package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.GradeDto;
import org.example.schoolapp.dto.request.GradeDtoRequest;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.EmployeeService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GradeMapper {
    private final EmployeeService employeeService;

    public GradeDto toGradeDto(Grade grade) {
        return new GradeDto().toBuilder()
                .id(grade.getId())
                .title(grade.getTitle())
                .classTeacher(employeeToString(grade))
                .studentCount(grade.getStudentSet().size())
                .creationDate(grade.getCreationDate())
                .isActive(grade.getIsActive())
                .build();
    }

    public List<GradeDto> toGradeDtoList(List<Grade> grades) {
        return grades.stream().map(this::toGradeDto).collect(Collectors.toList());
    }

    public Grade toGradeEntity(GradeDtoRequest gradeDtoRequest) {
        return new Grade().toBuilder()
                .id(gradeDtoRequest.getId())
                .title(gradeDtoRequest.getTitle())
                .classTeacher(employeeService.findByIdEntity(gradeDtoRequest.getClassTeacherId()))
                .creationDate(setCreationDate(gradeDtoRequest))
                .isActive(gradeDtoRequest.getIsActive())
                .build();
    }

    private LocalDateTime setCreationDate(GradeDtoRequest gradeDtoRequest) {
        if(gradeDtoRequest.getCreationDate() == null)
            return LocalDateTime.now();
        else
            return gradeDtoRequest.getCreationDate();
    }

    private String employeeToString(Grade grade) {
        User teacher = grade.getClassTeacher().getUser();

        String teacherFullName = teacher.getFirstName() + " " + teacher.getLastName();
        if (teacher.getMiddleName() != null)
            teacherFullName += " " + teacher.getMiddleName();

        return teacherFullName;
    }
}
