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
public class MarkDto {
    private Long id;
    private Integer mark;
    private Long studentId;
    private String studentName;
    private LessonDto lesson;
    private LocalDateTime gottenDate;
}
