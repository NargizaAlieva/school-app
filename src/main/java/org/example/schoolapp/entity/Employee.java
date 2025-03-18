package org.example.schoolapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employees")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position")
    private String position;

    @Min(value = 0, message = "Salary cannot be negative")
    @Column(name = "salary")
    private Integer salary;

    @NotNull(message = "User cannot be null")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classTeacher")
    private List<Grade> homeGrades;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacherSchedule")
    private List<Schedule> scheduleList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "m2m_subjects_teachers",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private Set<Subject> subjectSet;
}