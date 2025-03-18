package org.example.schoolapp.dto.response;

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
public class EmployeeDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    private String position;

    private Integer salary;

    @NotNull(message = "UserDto cannot be null")
    private UserDto userDto;

    private Set<String> subjectSet;
}
