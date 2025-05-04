package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsById(Long id);

    boolean existsByUserId(Long userId);

    Optional<Student> findByUserId(Long userId);

    @Query("SELECT s FROM Student s WHERE s.user.isActive = true")
    List<Student> findAllActiveStudents();

    @Query("SELECT s FROM Student s WHERE s.grade.id = :gradeId AND s.user.isActive = true")
    List<Student> findAllActiveStudentsByGrade(@Param("gradeId") Long gradeId);

    @Query("SELECT s FROM Student s WHERE s.grade.id IN :gradeIds AND s.user.isActive = true")
    List<Student> findAllActiveStudentsByGrades(@Param("gradeIds") List<Long> gradeIds);;

    @Query("SELECT s FROM Student s JOIN s.parent p WHERE p.id = :parentId")
    List<Student> findParentStudentsByParentId(@Param("parentId") Long parentId);
}