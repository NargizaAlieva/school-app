package org.example.schoolapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MarkDtoRequest {
    private Long id;
    private Integer mark;
    private Long studentId;
    private Long lessonId;
}
