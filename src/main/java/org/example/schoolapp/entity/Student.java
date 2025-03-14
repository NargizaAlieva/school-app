package org.example.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.schoolapp.enums.ParentStatus;

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

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "parent_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParentStatus parentStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Parent parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentMark")
    private List<Mark> markList;

    @ManyToOne
    @JoinColumn(name = "grade_id", referencedColumnName = "id")
    private Grade grade;
}