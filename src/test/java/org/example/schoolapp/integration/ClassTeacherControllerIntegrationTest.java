package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.request.MarkDtoRequest;
import org.example.schoolapp.dto.response.MarkDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.service.role.ClassTeacherService;
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
public class ClassTeacherControllerIntegrationTest {

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

    private String classTeacherToken;
    private Long gradeId;
    private Long studentId;
    private Long lessonId;
    @Autowired
    private ClassTeacherService classTeacherService;

    @BeforeEach
    void setUp() {
        Role teacherRole = roleRepository.findByTitle("CLASS_TEACHER").orElseThrow();
        Role studentRole = roleRepository.findByTitle("STUDENT").orElseThrow();

        User parentUser = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .password("password123")
                .isActive(true)
                .roleSet(new HashSet<>())
                .build();
        entityManager.persist(parentUser);

        Parent parent = Parent.builder()
                .user(parentUser)
                .build();
        entityManager.persist(parent);

        Subject subject = Subject.builder()
                .title("Music")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);

        User teacherUser = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("classteacher@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> teacherRoles = new HashSet<>();
        teacherRoles.add(teacherRole);
        teacherUser.setRoleSet(teacherRoles);
        entityManager.persist(teacherUser);

        Employee teacher = Employee.builder()
                .position("Class Teacher")
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

        User studentUser = User.builder()
                .firstName("Mark")
                .lastName("Smith")
                .email("student@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> studentRoles = new HashSet<>();
        studentRoles.add(studentRole);
        studentUser.setRoleSet(studentRoles);
        entityManager.persist(studentUser);

        Student student = Student.builder()
                .parentStatus(ParentStatus.FATHER)
                .user(studentUser)
                .grade(grade)
                .parent(parent)
                .build();
        entityManager.persist(student);
        studentId = student.getId();

        User studentUser2 = User.builder()
                .firstName("Sarah")
                .lastName("Johnson")
                .email("sarah@school.com")
                .password("password123")
                .isActive(true)
                .build();
        studentUser2.setRoleSet(studentRoles);
        entityManager.persist(studentUser2);

        Student student2 = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.MOTHER)
                .parent(parent)
                .user(studentUser2)
                .grade(grade)
                .build();
        entityManager.persist(student2);

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

        Lesson lesson = Lesson.builder()
                .topic("Algebra Basics")
                .homework("Complete exercises 1-10")
                .schedule(schedule)
                .build();
        entityManager.persist(lesson);
        lessonId = lesson.getId();

        entityManager.flush();

        Token classTeacherT = Token.builder()
                .token(jwtTokenUtil.generateToken(teacherUser))
                .tokenType(TokenType.BEARER)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.getJwtExpiration()))
                .build();

        entityManager.persist(classTeacherT);

        classTeacherToken = "Bearer " + classTeacherT.getToken();
    }

    @Test
    void createMark_ShouldCreateMark_WhenAuthenticatedAsClassTeacher() throws Exception {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setStudentId(studentId);
        request.setLessonId(lessonId);
        request.setMark(85);

        mockMvc.perform(post("/api/v1/class-teacher/create-mark")
                        .header("Authorization", classTeacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Mark."));
    }

    @Test
    void updateMark_ShouldUpdateMark_WhenAuthenticatedAsClassTeacher() throws Exception {
        MarkDtoRequest createRequest = new MarkDtoRequest();
        createRequest.setStudentId(studentId);
        createRequest.setLessonId(lessonId);
        createRequest.setMark(75);
        MarkDto createdMark = classTeacherService.createMark(createRequest);

        MarkDtoRequest updateRequest = new MarkDtoRequest();
        updateRequest.setId(createdMark.getId());
        updateRequest.setStudentId(studentId);
        updateRequest.setLessonId(lessonId);
        updateRequest.setMark(85);

        mockMvc.perform(put("/api/v1/class-teacher/update-mark")
                        .header("Authorization", classTeacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Mark."));
    }

    @Test
    void studentsFromClass_ShouldReturnForbidden_WhenNotAuthenticatedAsClassTeacher() throws Exception {
        User regularTeacherUser = User.builder()
                .firstName("Regular")
                .lastName("Teacher")
                .email("regular.teacher@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Role teacherRole = roleRepository.findByTitle("TEACHER").orElseThrow();
        Set<Role> teacherRoles = new HashSet<>();
        teacherRoles.add(teacherRole);
        regularTeacherUser.setRoleSet(teacherRoles);
        entityManager.persist(regularTeacherUser);

        Employee regularTeacher = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(regularTeacherUser)
                .build();
        entityManager.persist(regularTeacher);
        entityManager.flush();

        String regularTeacherToken = "Bearer " + jwtTokenUtil.generateToken(regularTeacherUser);

        mockMvc.perform(get("/api/v1/class-teacher/get-all-class-student")
                        .header("Authorization", regularTeacherToken))
                .andExpect(status().isFound());
    }

    @Test
    void createMark_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        MarkDtoRequest request = new MarkDtoRequest();

        mockMvc.perform(post("/api/v1/class-teacher/create-mark")
                        .header("Authorization", classTeacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMark_ShouldReturnNotFound_WhenMarkDoesNotExist() throws Exception {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setId(9999L);
        request.setStudentId(studentId);
        request.setLessonId(lessonId);
        request.setMark(85);

        mockMvc.perform(put("/api/v1/class-teacher/update-mark")
                        .header("Authorization", classTeacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllHomeGrades_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/class-teacher/get-all-home-grade"))
                .andExpect(status().isFound());
    }
}