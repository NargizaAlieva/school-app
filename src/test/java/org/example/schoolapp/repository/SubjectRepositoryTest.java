package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    private Subject math, physics, history;

    @BeforeEach
    void setUp() {
        math = new Subject();
        math.setTitle("Math");
        math.setIsActive(true);

        physics = new Subject();
        physics.setTitle("Physics");
        physics.setIsActive(false);

        history = new Subject();
        history.setTitle("History");
        history.setIsActive(true);

        subjectRepository.saveAll(List.of(math, physics, history));
    }

    @Test
    void testExistsById() {
        assertThat(subjectRepository.existsById(math.getId())).isTrue();
        assertThat(subjectRepository.existsById(999L)).isFalse();
    }

    @Test
    void testExistsByTitle() {
        assertThat(subjectRepository.existsByTitle("Math")).isTrue();
        assertThat(subjectRepository.existsByTitle("Biology")).isFalse();
    }

    @Test
    void testFindByTitle() {
        Optional<Subject> foundSubject = subjectRepository.findByTitle("Physics");
        assertThat(foundSubject).isPresent();
        assertThat(foundSubject.get().getTitle()).isEqualTo("Physics");
    }

    @Test
    void testFindByIsActiveTrue() {
        List<Subject> activeSubjects = subjectRepository.findByIsActiveTrue();
        assertThat(activeSubjects).hasSize(2).extracting(Subject::getTitle).contains("Math", "History");
    }

    @Test
    void testFindByTitles() {
        List<Subject> foundSubjects = subjectRepository.findByTitles(List.of("Math", "History"));
        assertThat(foundSubjects).hasSize(2).extracting(Subject::getTitle).contains("Math", "History");
    }

    @Test
    void testFindSubjectsByIds() {
        Set<Subject> foundSubjects = subjectRepository.findSubjectsByIds(Set.of(math.getId(), history.getId()));
        assertThat(foundSubjects).hasSize(2).extracting(Subject::getTitle).contains("Math", "History");
    }
}
