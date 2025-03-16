package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsById(Long id);

    @Query("SELECT l FROM Schedule s JOIN s.lessonSchedule l WHERE s.teacherSchedule.id = :teacherId")
    List<Lesson> getAllLessonsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT l FROM Schedule s JOIN s.lessonSchedule l WHERE s.gradeSchedule.id = :gradeId")
    List<Lesson> getAllLessonsByGradeId(@Param("gradeId") Long gradeId);

    @Query("SELECT l FROM Schedule s JOIN s.lessonSchedule l WHERE s.subjectSchedule.id = :subjectId")
    List<Lesson> getAllLessonsBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT l FROM Schedule s JOIN s.lessonSchedule l WHERE s.schoolYear = :year")
    List<Lesson> getAllLessonsByYear(@Param("year") String year);

    @Query("SELECT l FROM Schedule s JOIN s.lessonSchedule l WHERE s.quarter = :quarter")
    List<Lesson> getAllLessonsByQuarter(@Param("quarter") Integer quarter);

    @Query("SELECT l FROM Schedule s JOIN s.lessonSchedule l " +
            "WHERE s.subjectSchedule.id = :subjectId " +
            "AND s.gradeSchedule.id = :gradeId")
    List<Lesson> getAllLessonBySubjectQuarter(@Param("subjectId") Long subjectId,
                                              @Param("gradeId") Long gradeId);

}
