package org.example.schoolapp.repository;

import org.example.schoolapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByIsActiveTrue();

    @Query("SELECT u FROM User u JOIN u.roleSet r WHERE UPPER(r.title) = UPPER(:role)")
    List<User> findUsersByRole(@Param("role") String role);
}
