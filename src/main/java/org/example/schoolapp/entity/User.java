package org.example.schoolapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "login", nullable = false, unique = true)
    private String username;

    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 50, message = "Middle name must be at most 50 characters")
    @Column(name = "middle_name")
    private String middleName;

    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Phone number must be valid")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Email name cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "m2m_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleSet = new HashSet<>();;

    @PrePersist
    private void prePersist() {
        if (creationDate == null)
            creationDate = LocalDateTime.now();

        if (isActive == null)
            isActive = true;
    }
}