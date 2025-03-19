package org.example.schoolapp.dto.response;

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
    private Long id;
    private String topic;
    private String homework;
    private LocalDateTime creationDate;
    private ScheduleDto schedule;
}
