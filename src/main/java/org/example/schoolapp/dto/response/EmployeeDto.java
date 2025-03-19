package org.example.schoolapp.dto.response;

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
    private Long id;
    private String position;
    private Integer salary;
    private UserDto userDto;
    private Set<String> subjectSet;
}
