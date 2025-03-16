package org.example.schoolapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GradeDtoRequest {
    private Long id;
    private String title;
    private LocalDateTime creationDate;
    private Long classTeacherId;
    private Boolean isActive;
}
