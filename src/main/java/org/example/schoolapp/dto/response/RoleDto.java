package org.example.schoolapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    @NotNull(message = "Id cannot be null")
    Long id;

    @NotNull(message = "Role title cannot be null")
    @NotBlank(message = "Role title cannot be empty")
    @Size(min = 1, max = 50, message = "Role title must be between 1 and 50 characters")
    String title;
}
