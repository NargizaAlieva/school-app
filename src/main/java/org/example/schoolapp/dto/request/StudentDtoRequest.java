package org.example.schoolapp.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @NotNull(message = "Parent status cannot be null")
    @NotBlank(message = "Parent status cannot be empty")
    @Enumerated(EnumType.STRING)
    private String parentStatus;

    @NotNull(message = "UserId cannot be null")
    @NotBlank(message = "UserId cannot be empty")
    private Long userId;

    @NotNull(message = "ParentId cannot be null")
    @NotBlank(message = "ParentId cannot be empty")
    private Long parentId;

    @NotNull(message = "GradeId cannot be null")
    @NotBlank(message = "GradeId cannot be empty")
    private Long gradeId;
}
