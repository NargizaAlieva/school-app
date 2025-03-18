package org.example.schoolapp.dto.response;

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
import org.example.schoolapp.enums.ParentStatus;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @NotNull(message = "Parent status cannot be null")
    @Enumerated(EnumType.STRING)
    private ParentStatus parentStatus;

    @NotNull(message = "Grade title cannot be null")
    @NotBlank(message = "Grade title cannot be empty")
    private String gradeTitle;

    @NotNull(message = "ParentId cannot be null")
    private Long parentId;

    @NotNull(message = "UserDto cannot be null")
    private UserDto user;
}
