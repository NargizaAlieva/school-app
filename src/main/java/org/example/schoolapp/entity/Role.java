package org.example.schoolapp.entity;

import lombok.*;
import jakarta.persistence.*;

import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "roleSet", fetch = FetchType.LAZY)
    private Set<User> userSet;
}