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
public class SubjectDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isActive;
    private Set<String> teachersSet;
}
