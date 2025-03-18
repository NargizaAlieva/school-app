package org.example.schoolapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Subject name cannot be null")
    @NotBlank(message = "Subject title cannot be empty")
    @Size(max = 100, message = "Subject title must be at most 100 characters")
    private String title;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;

    private Set<String> teachersSet;
}
