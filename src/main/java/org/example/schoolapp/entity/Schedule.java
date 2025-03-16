package org.example.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.schoolapp.enums.DaysOfWeek;

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

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DaysOfWeek dayOfWeek;
    @Column(name = "quarter", nullable = false)
    private Integer quarter;
    @Column(name = "due_time", nullable = false)
    private String dueTime;
    @Column(name = "school_year", nullable = false)
    private String schoolYear;
    @Column(name = "is_approve")
    private Boolean isApprove;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subjectSchedule;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Employee teacherSchedule;

    @ManyToOne
    @JoinColumn(name = "grade_id", referencedColumnName = "id")
    private Grade gradeSchedule;

    @OneToMany(mappedBy = "schedule")
    private List<Lesson> lessonSchedule;

    @PrePersist
    public void prePersist() {
        if (isActive == null)
            isActive = false;
        if (isApprove == null)
            isApprove = false;
    }
}
