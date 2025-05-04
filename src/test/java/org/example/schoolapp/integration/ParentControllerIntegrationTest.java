package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ParentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTService jwtTokenUtil;

    @Autowired
    private RoleRepository roleRepository;

    private String parentToken;
    private Long parentId;
    private Long gradeId;
    private Long childUserId;
    private Long childId;

    @BeforeEach
    void setUp() {
        Role parentRole = roleRepository.findByTitle("PARENT").orElseThrow();
        Role studentRole = roleRepository.findByTitle("STUDENT").orElseThrow();

        User parentUser = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("parent@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> parentRoles = new HashSet<>();
        parentRoles.add(parentRole);
        parentUser.setRoleSet(parentRoles);
        entityManager.persist(parentUser);

        Parent parent = Parent.builder()
                .user(parentUser)
                .build();
        entityManager.persist(parent);
        parentId = parent.getId();

        User teacherUser = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("teacher@school.com")
                .password("password123")
                .roleSet(new HashSet<>())
                .isActive(true)
                .build();
        entityManager.persist(teacherUser);

        Employee teacher = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(teacherUser)
                .build();
        entityManager.persist(teacher);

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .classTeacher(teacher)
                .isActive(true)
                .build();
        entityManager.persist(grade);
        gradeId = grade.getId();

        Subject subject = Subject.builder()
                .title("Music")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);

        User childUser = User.builder()
                .firstName("Mark")
                .lastName("Smith")
                .email("student@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> studentRoles = new HashSet<>();
        studentRoles.add(studentRole);
        childUser.setRoleSet(studentRoles);
        entityManager.persist(childUser);
        childUserId = childUser.getId();

        Student child = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(childUser)
                .grade(grade)
                .parent(parent)
                .build();
        entityManager.persist(child);
        childId = child.getId();

        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(subject)
                .teacherSchedule(teacher)
                .gradeSchedule(grade)
                .build();
        entityManager.persist(schedule);

        entityManager.flush();

        Token parentT = Token.builder()
                .token(jwtTokenUtil.generateToken(parentUser))
                .tokenType(TokenType.BEARER)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.getJwtExpiration()))
                .build();

        entityManager.persist(parentT);

        parentToken = "Bearer " + parentT.getToken();
    }

    @Test
    void createStudent_ShouldCreateStudent_WhenAuthenticatedAsParent() throws Exception {
        StudentDtoRequest request = new StudentDtoRequest();
        request.setId(6L);
        request.setUserId(2L);
        request.setParentStatus("MOTHER");
        request.setGradeId(gradeId);
        request.setParentId(parentId);

        mockMvc.perform(post("/api/v1/parent/create-student")
                        .header("Authorization", parentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Student."));
    }

    @Test
    void getChildList_ShouldReturnChildren_WhenAuthenticatedAsParent() throws Exception {
        mockMvc.perform(get("/api/v1/parent/get-all-child")
                        .header("Authorization", parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all children."));
    }

    @Test
    void createStudent_ShouldReturnForbidden_WhenNotAuthenticated() throws Exception {
        StudentDtoRequest request = new StudentDtoRequest();
        request.setUserId(childUserId);
        request.setParentStatus("MOTHER");
        request.setGradeId(gradeId);
        request.setParentId(parentId);

        mockMvc.perform(post("/api/v1/parent/create-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isFound());
    }

    @Test
    void getChildList_ShouldReturnForbidden_WhenAuthenticatedAsNonParent() throws Exception {
        User teacherUser = User.builder()
                .firstName("Other")
                .lastName("Teacher")
                .email("other.teacher@school.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(teacherUser);
        String teacherToken = "Bearer " + jwtTokenUtil.generateToken(teacherUser);

        mockMvc.perform(get("/api/v1/parent/get-all-child")
                        .header("Authorization", teacherToken))
                .andExpect(status().isFound());
    }

    @Test
    void getStudentSchedule_ShouldReturnNotFound_ForNonExistentChild() throws Exception {
        mockMvc.perform(get("/api/v1/parent/get-student-schedule/{childId}", 9999L)
                        .header("Authorization", parentToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_ForNonExistentChild() throws Exception {
        mockMvc.perform(delete("/api/v1/parent/delete-user/{childId}", 9999L)
                        .header("Authorization", parentToken))
                .andExpect(status().isNotFound());
    }
}
