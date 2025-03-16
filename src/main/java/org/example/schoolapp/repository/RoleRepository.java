package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsById(Long id);
    boolean existsByTitle(String title);
}
