package org.example.schoolapp.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Mark cannot be null")
    @Min(value = 1, message = "Mark must be at least 1")
    @Max(value = 100, message = "Mark cannot exceed 100")
    private Integer mark;

    @NotNull(message = "StudentId cannot be null")
    private Long studentId;

    @NotNull(message = "Student name cannot be null")
    @NotBlank(message = "Student name cannot be empty")
    private String studentName;

    @NotNull(message = "LessonDto cannot be null")
    private LessonDto lesson;

    @NotNull(message = "Gotten date cannot be null")
    private LocalDateTime gottenDate;
}
