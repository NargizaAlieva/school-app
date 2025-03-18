package org.example.schoolapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.schoolapp.enums.ParentStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @NotNull(message = "Parent status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "parent_status", nullable = false)
    private ParentStatus parentStatus;

    @NotNull(message = "User cannot be null")
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @NotNull(message = "Parent cannot be null")
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Parent parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentMark")
    private List<Mark> markList = new ArrayList<>();

    @NotNull(message = "Grade cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "grade_id", referencedColumnName = "id", nullable = false)
    private Grade grade;

    @PrePersist
    public void prePersist() {
        if (markList == null)
            markList = new ArrayList<>();
    }
}