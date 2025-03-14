package org.example.schoolapp.bootstrup;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolapp.entity.Role;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.repository.SubjectRepository;
import org.example.schoolapp.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitData {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubjectRepository subjectRepository;

    @PostConstruct
    public void init(){
        log.warn("Init data");

        createUsers();
        createRoles();
        createSubjects();
    }

    private void createUsers() {
        User user1 = User.builder()
                .username("johndoe")
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .phone("+1234567890")
                .email("john@example.com")
                .password("password123")
                .build();

        User user2 = User.builder()
                .username("janedoe")
                .firstName("Jane")
                .lastName("Doe")
                .middleName("Elizabeth")
                .phone("+9876543210")
                .email("jane@example.com")
                .password("securePass456")
                .build();

        User user3 = User.builder()
                .username("alexsmith")
                .firstName("Alex")
                .lastName("Smith")
                .middleName("Robert")
                .phone("+1122334455")
                .email("alex@example.com")
                .password("strongPassword789")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    private void createRoles() {
        Role role1 = Role.builder()
                .title("CLASS_TEACHER")
                .build();

        Role role2 = Role.builder()
                .title("ADMIN")
                .build();

        Role role3 = Role.builder()
                .title("PRINCIPAL")
                .build();

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);
    }

    private void createSubjects() {
        Subject subject1 = Subject.builder()
                .title("Mathematics")
                .description("This subject covers algebra, calculus, and geometry.")
                .build();

        Subject subject2 = Subject.builder()
                .title("History")
                .description("Covers world history, ancient civilizations, and modern events.")
                .build();

        Subject subject3 = Subject.builder()
                .title("Chemistry")
                .description("Explores organic, inorganic, and physical chemistry.")
                .build();

        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        subjectRepository.save(subject3);
    }
}
