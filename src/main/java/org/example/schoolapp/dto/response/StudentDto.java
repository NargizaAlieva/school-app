package org.example.schoolapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.schoolapp.enums.ParentStatus;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private Date birthday;
    private ParentStatus parentStatus;
    private Long parentId;
    private UserDto user;
    private String gradeTitle;
}
