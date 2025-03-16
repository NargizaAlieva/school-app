package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Subject;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SubjectMapper {

    public SubjectDto toSubjectDto(Subject subject) {
        return new SubjectDto().toBuilder()
                .id(subject.getId())
                .title(subject.getTitle())
                .description(subject.getDescription())
                .teachersSet(employeeToString(subject))
                .isActive(subject.getIsActive())
                .build();
    }

    public List<SubjectDto> toSubjectDtoList(List<Subject> subjects) {
        return subjects.stream().map(this::toSubjectDto).collect(Collectors.toList());
    }

    public Set<SubjectDto> toSubjectDtoSet(Set<Subject> subjects) {
        return subjects.stream().map(this::toSubjectDto).collect(Collectors.toSet());
    }

    public Subject toSubjectEntity(SubjectDtoRequest subjectDtoRequest) {
        return new Subject().toBuilder()
                .id(subjectDtoRequest.getId())
                .title(subjectDtoRequest.getTitle())
                .description(subjectDtoRequest.getDescription())
                .isActive(subjectDtoRequest.getIsActive())
                .build();
    }

    private Set<String> employeeToString(Subject subject) {
        Set<String> teacherSet = new HashSet<>();

        if (subject.getTeachersSet() == null) {
            return teacherSet;
        }

        for (Employee teacher : subject.getTeachersSet()) {
            String fullName = teacher.getUser().getFirstName() + " " + teacher.getUser().getLastName();

            if (teacher.getUser().getMiddleName() != null && !teacher.getUser().getMiddleName().isEmpty())
                fullName += " " + teacher.getUser().getMiddleName();

            teacherSet.add(fullName);
        }

        return teacherSet;
    }
}
