package org.example.schoolapp.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.dto.response.*;
import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.service.entity.*;
import org.example.schoolapp.service.role.ParentRoleService;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.StudentMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParentRoleServiceImpl implements ParentRoleService {
    private final UserService userService;
    private final MarkService markService;
    private final ScheduleService scheduleService;
    private final StudentService studentService;
    private final ParentService parentService;
    private final StudentMapper studentMapper;

    public Parent getCurrentStudent() {
        return parentService.getParentByUserId(userService.getCurrentUser().getId());
    }
    public Student getChild(Long childId) {
        if (getCurrentStudent().getChildrenList().size() < childId)
            throw new ObjectNotFoundException("Child");
        return getCurrentStudent().getChildrenList().get(Math.toIntExact(childId-1));
    }
    @Override
    public Map<String, StudentDto> getChildList() {
        Map<String, StudentDto> studentDtoMap = new HashMap<>();
        for (int i = 1; i <= getCurrentStudent().getChildrenList().size(); i++) {
            String childIndex = "Child " + i;
            StudentDto childDto = studentMapper.toStudentDto(getChild((long) i));
            studentDtoMap.put(childIndex, childDto);
        }
        return studentDtoMap;
    }
    @Override
    public StudentDto createStudent(StudentDtoRequest studentDtoRequest) {
        return studentService.createStudent(studentDtoRequest);
    }

    @Override
    public List<MarkDto> getAllMark(Long childId) {
        Student child = getChild(childId);
        return markService.getAllMarkByStudent(child.getId());    }

    @Override
    public List<ScheduleDto> getStudentSchedule(Long childId) {
        Student child = getChild(childId);
        return scheduleService.getAllScheduleByStudent(child.getId());
    }

    @Override
    public void leaveSchool(Long childId) {
        Student child = getChild(childId);
        userService.deleteUser(child.getUser().getId());
    }
}
