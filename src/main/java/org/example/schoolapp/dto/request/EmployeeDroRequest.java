package org.example.schoolapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDroRequest {
    private Long id;

    private String position;

    private Integer salary;

    @NotNull(message = "UserId cannot be null")
    private Long userId;

    private Set<Long> subjectSet;
}
