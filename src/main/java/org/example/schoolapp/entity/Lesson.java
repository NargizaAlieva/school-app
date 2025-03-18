package org.example.schoolapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Topic cannot be null")
    @NotBlank(message = "Topic cannot be blank")
    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "homework")
    private String homework;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @NotNull(message = "Schedule cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private Schedule schedule;

    @OneToMany(mappedBy = "lessonMark", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mark> markList = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (creationDate == null)
            creationDate = LocalDateTime.now();
    }
}