package org.example.schoolapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDtoRequest {
    private Long id;
    private Date birthday;
    private String parentStatus;
    private Long userId;
    private Long parentId;
    private Long gradeId;
}
