package org.example.schoolapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.schoolapp.enums.DaysOfWeek;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private Long id;
    private DaysOfWeek dayOfWeek;
    private Integer quarter;
    private String dueTime;
    private String schoolYear;
    private Long subjectId;
    private String subjectTitle;
    private String teacherName;
    private Long gradeId;
    private String gradeName;
    private Boolean isApprove;
    private Boolean isActive;
}
