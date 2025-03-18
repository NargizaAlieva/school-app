package org.example.schoolapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.schoolapp.enums.DaysOfWeek;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Day of the week cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DaysOfWeek dayOfWeek;

    @NotNull(message = "Quarter cannot be null")
    @Min(value = 1, message = "Quarter must be at least 1")
    @Max(value = 4, message = "Quarter cannot exceed 4")
    @Column(name = "quarter", nullable = false)
    private Integer quarter;

    @NotNull(message = "Due time cannot be null")
    @NotBlank(message = "Due time cannot be empty")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d-(?:[01]\\d|2[0-3]):[0-5]\\d$",
            message = "Due time must be in HH:mm-HH:mm format")
    @Column(name = "due_time", nullable = false)
    private String dueTime;

    @NotNull(message = "School year cannot be null")
    @NotBlank(message = "School year cannot be empty")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "School year must be in YYYY-YYYY format")
    @Column(name = "school_year", nullable = false)
    private String schoolYear;

    @Column(name = "is_approve", nullable = false)
    private Boolean isApprove = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @NotNull(message = "Subject cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", referencedColumnName = "id", nullable = false)
    private Subject subjectSchedule;

    @NotNull(message = "Teacher cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private Employee teacherSchedule;

    @NotNull(message = "Grade cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "grade_id", referencedColumnName = "id", nullable = false)
    private Grade gradeSchedule;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonSchedule = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (isApprove == null) isApprove = false;
        if (isActive == null) isActive = false;
    }
}