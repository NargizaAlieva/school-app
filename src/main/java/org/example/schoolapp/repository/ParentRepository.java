package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    boolean existsById(Long id);

    @Query("SELECT p FROM Parent p WHERE p.user.isActive = true")
    List<Parent> findAllActiveParents();
}
