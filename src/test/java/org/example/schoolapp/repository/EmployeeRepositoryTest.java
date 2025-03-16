package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Employee employee;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("rosa");
        user.setFirstName("Rosa");
        user.setLastName("Ala");
        user.setEmail("rosa@example.com");
        user.setPassword("password");
        user.setIsActive(true);
        user = userRepository.save(user);

        employee = new Employee();
        employee.setUser(user);
        employee = employeeRepository.save(employee);
    }

    @Test
    void testExistsById() {
        boolean exists = employeeRepository.existsById(employee.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserId() {
        boolean exists = employeeRepository.existsByUserId(user.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByUserId() {
        Optional<Employee> foundEmployee = employeeRepository.findByUserId(user.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void testFindAllActiveEmployees() {
        List<Employee> activeEmployees = employeeRepository.findAllActiveEmployees();
        assertThat(activeEmployees).isNotEmpty();
    }
}
