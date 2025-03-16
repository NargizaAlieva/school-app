package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setTitle("Admin");
        role = roleRepository.save(role);
    }

    @Test
    void testExistsById() {
        boolean exists = roleRepository.existsById(role.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByTitle() {
        boolean exists = roleRepository.existsByTitle("Admin");
        assertThat(exists).isTrue();
    }

    @Test
    void testDoesNotExistByTitle() {
        boolean exists = roleRepository.existsByTitle("User");
        assertThat(exists).isFalse();
    }
}