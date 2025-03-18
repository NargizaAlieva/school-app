package org.example.schoolapp.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParentDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "UserDto cannot be null")
    private UserDto user;

    private List<String> childrenNameList;
}
