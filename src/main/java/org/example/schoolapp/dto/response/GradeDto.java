package org.example.schoolapp.dto.response;

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
public class GradeDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime creationDate;

    @NotNull(message = "Class Teacher cannot be null")
    @NotBlank(message = "Class Teacher cannot be blank")
    private String classTeacher;

    @NotNull(message = "Student count cannot be null")
    private Integer studentCount;

    @NotNull(message = "IsActive cannot be null")
    private Boolean isActive;
}
