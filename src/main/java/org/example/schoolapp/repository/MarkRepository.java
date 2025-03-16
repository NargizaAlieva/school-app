package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    boolean existsById(Long id);

    List<Mark> findByStudentMarkId(Long studentId);

    @Query("SELECT m FROM Mark m " +
            "JOIN m.lessonMark l " +
            "JOIN l.schedule s " +
            "WHERE m.studentMark.id = :studentId " +
            "AND s.schoolYear = :year " +
            "AND s.quarter = :quarter " +
            "AND s.subjectSchedule.id = :subjectId")
    List<Mark> findMarksByStudentAndSchedule(@Param("studentId") Long studentId,
                                             @Param("subjectId") Long subjectId,
                                             @Param("year") String year,
                                             @Param("quarter") Integer quarter);

    @Query("SELECT m FROM Mark m " +
            "JOIN m.lessonMark l " +
            "JOIN l.schedule s " +
            "WHERE m.studentMark.id = :studentId " +
            "AND s.schoolYear = :year " +
            "AND s.quarter = :quarter")
    List<Mark> findMarksByStudentAndSchedule(@Param("studentId") Long studentId,
                                             @Param("year") String year,
                                             @Param("quarter") Integer quarter);

    @Query("SELECT m FROM Mark m " +
            "JOIN m.lessonMark l " +
            "JOIN l.schedule s " +
            "WHERE m.studentMark.id = :studentId " +
            "AND s.schoolYear = :year")
    List<Mark> findMarksByStudentAndSchedule(@Param("studentId") Long studentId,
                                             @Param("year") String year);

    @Query("SELECT AVG(m.mark) FROM Mark m WHERE m.studentMark.id = :studentId")
    Double findAverageMarkByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT AVG(m.mark) FROM Mark m WHERE m.studentMark.id = :studentId AND m.lessonMark.schedule.subjectSchedule.id = :subjectId")
    Double findAverageMarkByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);
}
