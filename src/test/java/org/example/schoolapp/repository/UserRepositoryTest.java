package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        Role role = new Role();
        role.setTitle("ROLE_ADMIN");
        entityManager.persist(role);

        user = User.builder()
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .roleSet(new HashSet<>())
                .build();

        user.getRoleSet().add(role);

        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnUser() {
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenExistsByUsername_thenReturnTrue() {
        boolean exists = userRepository.existsByUsername(user.getUsername());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        boolean exists = userRepository.existsByEmail(user.getEmail());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByIsActiveTrue_thenReturnActiveUsers() {
        List<User> activeUsers = userRepository.findByIsActiveTrue();
        assertThat(activeUsers).isNotEmpty();
        assertThat(activeUsers.get(0).getIsActive()).isTrue();
    }

    @Test
    public void whenFindUsersByRole_thenReturnUsersWithRole() {
        List<User> usersWithRole = userRepository.findUsersByRole("ROLE_ADMIN");
        assertThat(usersWithRole).isNotEmpty();
        assertThat(usersWithRole.get(0).getRoleSet()).extracting(Role::getTitle).contains("ROLE_ADMIN");
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = userRepository.existsById(user.getId());
        assertThat(exists).isTrue();
    }
}