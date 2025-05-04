package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ParentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParentRepository parentRepository;

    private Parent parent;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(user);

        parent = Parent.builder()
                .user(user)
                .build();
        entityManager.persist(parent);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnParent() {
        Parent foundParent = parentRepository.findById(parent.getId()).orElse(null);
        assertThat(foundParent).isNotNull();
        assertThat(foundParent.getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = parentRepository.existsById(parent.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindAllActiveParents_thenReturnActiveParents() {
        List<Parent> activeParents = parentRepository.findAllActiveParents();
        assertThat(activeParents).isNotEmpty();
        assertThat(activeParents.get(0).getUser().getIsActive()).isTrue();
    }
}