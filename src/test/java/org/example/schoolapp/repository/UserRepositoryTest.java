package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setTitle("CLASS_TEACHER");

        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("securepassword");
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setIsActive(true);
        user.setRoleSet(Set.of(role));
        userRepository.save(user);
    }

    @Test
    void testExistsById() {
        assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    void testExistsByUsername() {
        assertTrue(userRepository.existsByUsername("john_doe"));
    }

    @Test
    void testExistsByEmail() {
        assertTrue(userRepository.existsByEmail("john@example.com"));
    }

    @Test
    void testFindByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("john_doe");
        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("john_doe", foundUser.get().getUsername());
    }

    @Test
    void testFindByIsActiveTrue() {
        List<User> activeUsers = userRepository.findByIsActiveTrue();
        assertEquals(1, activeUsers.size());
        assertEquals("john_doe", activeUsers.get(0).getUsername());
    }

    @Test
    void testFindUsersByRole() {
        List<User> usersByRole = userRepository.findUsersByRole("CLASS_TEACHER");
        assertEquals(1, usersByRole.size());
        assertEquals("john_doe", usersByRole.get(0).getUsername());
    }
}