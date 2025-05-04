package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsById(Long id);

    @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.user.id = :userId AND e.user.isActive = true")
    Boolean existsByUserId(@Param("userId") Long userId);

    Optional<Employee> findByUserId(Long userId);

    @Query("SELECT e FROM Employee e JOIN e.subjectSet s WHERE s.id = :subjectId")
    List<Employee> findBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT e FROM Employee e WHERE e.user.isActive = true")
    List<Employee> findAllActiveEmployees();

    @Query("SELECT g.classTeacher FROM Grade g WHERE g.id = :gradeId")
    Employee getHomeTeacherByGradeId(@Param("gradeId") Long gradeId);

    @Query("SELECT g.id FROM Employee e JOIN e.homeGrades g WHERE e.id = :employeeId")
    List<Long> findHomeGradeIdsByEmployeeId(@Param("employeeId") Long employeeId);
}
