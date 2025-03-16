package org.example.schoolapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDtoRequest {
    private Long id;
    private String dayOfWeek;
    private Integer quarter;
    private String dueTime;
    private String schoolYear;
    private Boolean isApprove;
    private Boolean isActive;
    private Long subjectId;
    private Long teacherId;
    private Long gradeId;
}
