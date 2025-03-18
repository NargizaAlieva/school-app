package org.example.schoolapp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MarkDtoRequest {
    private Long id;

    @NotNull(message = "Mark cannot be null")
    @Min(value = 1, message = "Mark must be at least 1")
    @Max(value = 100, message = "Mark cannot exceed 100")
    private Integer mark;

    @NotNull(message = "StudentId cannot be null")
    private Long studentId;

    @NotNull(message = "LessonId cannot be null")
    private Long lessonId;
}
