package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Lesson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        lesson = new Lesson();
        lesson.setTopic("Mathematics");
        lesson = lessonRepository.save(lesson);
    }

    @Test
    void testExistsById() {
        boolean exists = lessonRepository.existsById(lesson.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testFindById() {
        Optional<Lesson> foundLesson = lessonRepository.findById(lesson.getId());
        assertThat(foundLesson).isPresent();
        assertThat(foundLesson.get().getTopic()).isEqualTo("Mathematics");
    }

    @Test
    void testFindAll() {
        List<Lesson> lessons = lessonRepository.findAll();
        assertThat(lessons).isNotEmpty();
    }

    @Test
    void testDeleteById() {
        lessonRepository.deleteById(lesson.getId());
        boolean exists = lessonRepository.existsById(lesson.getId());
        assertThat(exists).isFalse();
    }
}