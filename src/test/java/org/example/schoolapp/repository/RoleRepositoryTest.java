package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setUp() {
        role = Role.builder()
                .title("ROLE_ADMIN")
                .build();

        entityManager.persist(role);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnRole() {
        Optional<Role> foundRole = roleRepository.findById(role.getId());
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getTitle()).isEqualTo(role.getTitle());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = roleRepository.existsById(role.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByTitle_thenReturnTrue() {
        boolean exists = roleRepository.existsByTitle(role.getTitle());
        assertThat(exists).isTrue();
    }
}