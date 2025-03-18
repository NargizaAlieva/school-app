package org.example.schoolapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParentDtoRequest {
    private Long id;

    @NotNull(message = "UserId cannot be null")
    private Long userId;
}
