package org.example.schoolapp.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
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

    @NotNull(message = "Role title cannot be null")
    @NotBlank(message = "Role title cannot be empty")
    @Size(min = 1, max = 50, message = "Role title must be between 1 and 50 characters")
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "roleSet", fetch = FetchType.EAGER)
    private Set<User> userSet = new HashSet<>();
}
