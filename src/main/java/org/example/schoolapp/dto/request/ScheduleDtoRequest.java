package org.example.schoolapp.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDtoRequest {
    private Long id;

    @NotNull(message = "Day of the week cannot be null")
    @NotBlank(message = "Day of the week cannot be empty")
    @Enumerated(EnumType.STRING)
    private String dayOfWeek;

    @NotNull(message = "Quarter cannot be null")
    @Min(value = 1, message = "Quarter must be at least 1")
    @Max(value = 4, message = "Quarter cannot exceed 4")
    private Integer quarter;

    @NotNull(message = "Due time cannot be null")
    @NotBlank(message = "Due time cannot be empty")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d-(?:[01]\\d|2[0-3]):[0-5]\\d$",
            message = "Due time must be in HH:mm-HH:mm format")
    private String dueTime;

    @NotNull(message = "School year cannot be null")
    @NotBlank(message = "School year cannot be empty")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "School year must be in YYYY-YYYY format")
    private String schoolYear;

    @NotNull(message = "SubjectId cannot be null")
    private Long subjectId;

    @NotNull(message = "TeacherId cannot be null")
    private Long teacherId;

    @NotNull(message = "GradeId cannot be null")
    private Long gradeId;

    private Boolean isApprove;

    private Boolean isActive;
}
