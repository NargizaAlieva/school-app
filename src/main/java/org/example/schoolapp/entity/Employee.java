package org.example.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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
    @Column(name = "salary")
    private Integer salary;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classTeacher")
    private List<Grade> homeGrades;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacherSchedule")
    private List<Schedule> scheduleList;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "teachersSet")
    private Set<Subject> subjectSet = new HashSet<>();
}
