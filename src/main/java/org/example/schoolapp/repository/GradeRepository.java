package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    boolean existsById(Long id);

    List<Grade> findAllByIsActiveTrue();

    List<Grade> findAllByIdIn(Set<Long> gradeIds);

    List<Grade> findByClassTeacherId(Long teacherId);

    @Query("SELECT g FROM Grade g JOIN g.studentSet s WHERE s.id = :studentId")
    Grade getGradeByStudentId(@Param("studentId") Long studentId);
}
