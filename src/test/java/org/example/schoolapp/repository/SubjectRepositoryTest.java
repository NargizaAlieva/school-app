package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Subject;
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
public class SubjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubjectRepository subjectRepository;

    private Subject subject;

    @BeforeEach
    public void setUp() {
        subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();

        entityManager.persist(subject);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnSubject() {
        Optional<Subject> foundSubject = subjectRepository.findById(subject.getId());
        assertThat(foundSubject).isPresent();
        assertThat(foundSubject.get().getTitle()).isEqualTo(subject.getTitle());
    }

    @Test
    public void whenFindByTitle_thenReturnSubject() {
        Optional<Subject> foundSubject = subjectRepository.findByTitle(subject.getTitle());
        assertThat(foundSubject).isPresent();
        assertThat(foundSubject.get().getTitle()).isEqualTo(subject.getTitle());
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        boolean exists = subjectRepository.existsById(subject.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByTitle_thenReturnTrue() {
        boolean exists = subjectRepository.existsByTitle(subject.getTitle());
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByIsActiveTrue_thenReturnActiveSubjects() {
        List<Subject> activeSubjects = subjectRepository.findByIsActiveTrue();
        assertThat(activeSubjects).isNotEmpty();
        assertThat(activeSubjects.get(0).getIsActive()).isTrue();
    }

    @Test
    public void whenFindByTitles_thenReturnSubjects() {
        List<String> titles = List.of("Mathematics", "Physics");
        List<Subject> foundSubjects = subjectRepository.findByTitles(titles);

        assertThat(foundSubjects).isNotEmpty();
        assertThat(foundSubjects.get(0).getTitle()).isEqualTo(subject.getTitle());
    }

    @Test
    public void whenFindSubjectsByIds_thenReturnSubjects() {
        Set<Long> subjectIds = Set.of(subject.getId());
        Set<Subject> foundSubjects = subjectRepository.findSubjectsByIds(subjectIds);

        assertThat(foundSubjects).isNotEmpty();
        assertThat(foundSubjects.iterator().next().getId()).isEqualTo(subject.getId());
    }
}