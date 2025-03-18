package org.example.schoolapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Topic cannot be null")
    @NotBlank(message = "Topic cannot be blank")
    private String topic;

    private String homework;

    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime creationDate;

    @NotNull(message = "ScheduleDto cannot be null")
    private ScheduleDto schedule;
}
