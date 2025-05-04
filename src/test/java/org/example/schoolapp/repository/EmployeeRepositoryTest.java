package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Grade;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private User user;
    private Subject subject;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user);

        subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);

        employee = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(user)
                .subjectSet(Set.of(subject))
                .build();
        entityManager.persist(employee);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnEmployee() {
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getPosition()).isEqualTo(employee.getPosition());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = employeeRepository.existsById(employee.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByUserId_thenReturnTrue() {
        boolean exists = employeeRepository.existsByUserId(user.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByUserId_thenReturnEmployee() {
        Optional<Employee> foundEmployee = employeeRepository.findByUserId(user.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void whenFindBySubjectId_thenReturnEmployees() {
        List<Employee> foundEmployees = employeeRepository.findBySubjectId(subject.getId());
        assertThat(foundEmployees).isNotEmpty();
        assertThat(foundEmployees.get(0).getSubjectSet()).contains(subject);
    }

    @Test
    public void whenFindAllActiveEmployees_thenReturnActiveEmployees() {
        List<Employee> activeEmployees = employeeRepository.findAllActiveEmployees();
        assertThat(activeEmployees).isNotEmpty();
        assertThat(activeEmployees.get(0).getUser().getIsActive()).isTrue();
    }

    @Test
    public void whenGetHomeTeacherByGradeId_thenReturnEmployee() {
        User homeTeacherUser = User.builder()
                .firstName("Home")
                .lastName("Teacher")
                .email("hometeacher@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(homeTeacherUser);

        Employee homeTeacher = Employee.builder()
                .position("Home Teacher")
                .salary(60000)
                .user(homeTeacherUser)
                .build();
        entityManager.persist(homeTeacher);

        Grade grade = Grade.builder()
                .title("10B")
                .classTeacher(homeTeacher)
                .build();
        entityManager.persist(grade);
        entityManager.flush();

        Employee foundHomeTeacher = employeeRepository.getHomeTeacherByGradeId(grade.getId());
        assertThat(foundHomeTeacher).isNotNull();
        assertThat(foundHomeTeacher.getId()).isEqualTo(homeTeacher.getId());
    }
}