package org.example.schoolapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @NotNull(message = "Subject name cannot be null")
    @NotBlank(message = "Subject title cannot be empty")
    @Size(max = 100, message = "Subject title must be at most 100 characters")
    @Column(name = "subject_title", nullable = false, unique = true)
    private String title;

    @Size(max = 255, message = "Description must be at most 255 characters")
    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjectSet")
    private Set<Employee> teachersSet = new HashSet<>();

    @PrePersist
    private void prePersist() {
        if (isActive == null)
            isActive = true;
    }
}