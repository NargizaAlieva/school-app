package org.example.schoolapp.dto.request;

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
public class LessonDtoRequest {
    private Long id;

    @NotNull(message = "Topic cannot be null")
    @NotBlank(message = "Topic cannot be blank")
    private String topic;

    private String homework;

    private LocalDateTime creationDate;

    @NotNull(message = "ScheduleId cannot be null")
    private Long scheduleId;
}
