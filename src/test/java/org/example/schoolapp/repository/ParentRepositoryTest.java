package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Parent;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.ParentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ParentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Parent activeParent;
    private Parent inactiveParent;
    private Student student;

    @BeforeEach
    void setUp() {
        User studentUser = new User();
        studentUser.setUsername("studentUser");
        studentUser.setFirstName("studentUser");
        studentUser.setLastName("studentUser");
        studentUser.setEmail("studentUser@activeUser.com");
        studentUser.setPassword("password");
        studentUser.setIsActive(true);
        userRepository.save(studentUser);

        User activeUser = new User();
        activeUser.setUsername("activeUser");
        activeUser.setFirstName("activeUser");
        activeUser.setLastName("activeUser");
        activeUser.setEmail("activeUser@activeUser.com");
        activeUser.setPassword("password");
        activeUser.setIsActive(true);
        userRepository.save(activeUser);

        User inactiveUser = new User();
        inactiveUser.setUsername("inactiveUser");
        inactiveUser.setFirstName("inactiveUser");
        inactiveUser.setLastName("inactiveUser");
        inactiveUser.setEmail("inactiveUser@activeUser.com");
        inactiveUser.setPassword("password");
        inactiveUser.setIsActive(false);
        userRepository.save(inactiveUser);

        student = new Student();
        student.setUser(studentUser);
        student.setParentStatus(ParentStatus.MOTHER);
        student.setParent(activeParent);
        studentRepository.save(student);

        activeParent = new Parent();
        activeParent.setUser(activeUser);
        parentRepository.save(activeParent);

        inactiveParent = new Parent();
        inactiveParent.setUser(inactiveUser);
        parentRepository.save(inactiveParent);
    }

    @Test
    void testFindAllActiveParents() {
        List<Parent> activeParents = parentRepository.findAllActiveParents();
        assertThat(activeParents).contains(activeParent);
        assertThat(activeParents).doesNotContain(inactiveParent);
    }
}