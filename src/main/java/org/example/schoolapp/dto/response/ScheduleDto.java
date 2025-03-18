package org.example.schoolapp.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.schoolapp.enums.DaysOfWeek;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Day of the week cannot be null")
    @Enumerated(EnumType.STRING)
    private DaysOfWeek dayOfWeek;

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

    @NotNull(message = "Subject title cannot be null")
    @NotBlank(message = "Subject title cannot be empty")
    private String subjectTitle;

    @NotNull(message = "Teacher name cannot be null")
    @NotBlank(message = "Teacher name cannot be empty")
    private String teacherName;

    @NotNull(message = "GradeId cannot be null")
    private Long gradeId;

    @NotNull(message = "Grade name cannot be null")
    @NotBlank(message = "Grade name cannot be empty")
    private String gradeName;

    @NotNull(message = "isApprove cannot be null")
    private Boolean isApprove;

    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;
}
