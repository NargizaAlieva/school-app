package org.example.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjectSet")
    private Set<Employee> teachersSet;

    @PrePersist
    private void prePersist() {
        if (isActive == null)
            isActive = true;

        if (teachersSet == null)
            teachersSet = new HashSet<>();
    }
}
