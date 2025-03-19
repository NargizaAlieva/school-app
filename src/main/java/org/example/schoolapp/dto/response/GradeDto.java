package org.example.schoolapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GradeDto {
    private Long id;
    private String title;
    private LocalDateTime creationDate;
    private String classTeacher;
    private Integer studentCount;
    private Boolean isActive;
}
