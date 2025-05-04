package org.example.schoolapp.util.mapper;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.response.ParentDto;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.service.entity.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParentMapper {
    private final UserMapper userMapper;
    private final UserService userService;

    public ParentDto toParentDto(Parent parent) {
        return new ParentDto().toBuilder()
                .id(parent.getId())
                .user(userMapper.toUserDto(parent.getUser()))
                .childrenNameList(childrenToString(parent))
                .build();
    }

    public List<ParentDto> toParentDtoList(List<Parent> parents) {
        return parents.stream().map(this::toParentDto).collect(Collectors.toList());
    }

    public Parent toParentEntity(ParentDtoRequest parentDtoRequest) {
        User user = userService.getEntityById(parentDtoRequest.getUserId());

        return new Parent().toBuilder()
                .id(parentDtoRequest.getId())
                .user(user)
                .build();
    }

    private List<String> childrenToString(Parent parent) {
        List<String> childrenName = new ArrayList<>();

        for (Student s : parent.getChildrenList()) {
            String fullName = s.getUser().getFirstName() + " " + s.getUser().getLastName();
            if (s.getUser().getMiddleName() != null)
                fullName += " " + s.getUser().getMiddleName();

            childrenName.add(fullName);
        }

        return childrenName;
    }
}
