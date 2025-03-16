package org.example.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "grades")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_title")
    private String title;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Employee classTeacher;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "grade")
    private Set<Student> studentSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gradeSchedule")
    private List<Schedule> scheduleList;

    @PrePersist
    private void prePersist() {
        if(creationDate == null)
            creationDate = LocalDateTime.now();

        if (isActive == null)
            isActive = true;

        if (studentSet == null)
            studentSet = new HashSet<>();
    }
}
