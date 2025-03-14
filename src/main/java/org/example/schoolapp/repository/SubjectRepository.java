package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsById(Long id);
    boolean existsByTitle(String title);

    Optional<Subject> findByTitle(String title);

    List<Subject> findByIsActiveTrue();

    @Query("SELECT s FROM Subject s WHERE s.title IN :titles")
    List<Subject> findByTitles(@Param("titles") List<String> titles);

    @Query("SELECT s FROM Subject s WHERE s.id IN :subjectIds")
    Set<Subject> findSubjectsByIds(@Param("subjectIds") Set<Long> subjectIds);
}
