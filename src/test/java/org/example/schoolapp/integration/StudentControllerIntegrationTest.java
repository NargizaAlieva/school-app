package org.example.schoolapp.integration;

import jakarta.persistence.EntityManager;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
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
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JWTService jwtTokenUtil;

    @Autowired
    private RoleRepository roleRepository;

    private String studentToken;
    private String teacherToken;
    private Long subjectId;

    @BeforeEach
    void setUp() {
        // Create roles if they don't exist
        Role studentRole = roleRepository.findByTitle("STUDENT").orElseThrow();

        Role teacherRole = roleRepository.findByTitle("TEACHER").orElseThrow();

        User parentUser = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(parentUser);

        Parent parent = Parent.builder()
                .user(parentUser)
                .build();
        entityManager.persist(parent);

        Subject subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);
        subjectId = subject.getId();

        User teacherUser = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("teacher@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> teacherRoles = new HashSet<>();
        teacherRoles.add(teacherRole);
        teacherUser.setRoleSet(teacherRoles);
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
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(studentUser)
                .grade(grade)
                .parent(parent)
                .build();
        entityManager.persist(student);

        User classmateUser = User.builder()
                .firstName("Sarah")
                .lastName("Johnson")
                .email("sarah@school.com")
                .password("password123")
                .isActive(true)
                .build();
        classmateUser.setRoleSet(studentRoles);
        entityManager.persist(classmateUser);

        Student classmate = Student.builder()
                .birthday(new Date())
                .parent(parent)
                .parentStatus(ParentStatus.MOTHER)
                .user(classmateUser)
                .grade(grade)
                .build();
        entityManager.persist(classmate);

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

        Mark mark1 = Mark.builder()
                .mark(85)
                .studentMark(student)
                .lessonMark(lesson)
                .build();
        entityManager.persist(mark1);

        Mark mark2 = Mark.builder()
                .mark(90)
                .studentMark(student)
                .lessonMark(lesson)
                .build();
        entityManager.persist(mark2);

        entityManager.flush();

        Token studentT = Token.builder()
                        .token(jwtTokenUtil.generateToken(studentUser))
                        .tokenType(TokenType.BEARER)
                        .createdAt(new Date(System.currentTimeMillis()))
                        .expiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.getJwtExpiration()))
                        .build();

        Token teacherT = Token.builder()
                .token(jwtTokenUtil.generateToken(teacherUser))
                .tokenType(TokenType.BEARER)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.getJwtExpiration()))
                .build();

        entityManager.persist(studentT);
        entityManager.persist(teacherT);

        studentToken = "Bearer " + studentT.getToken();
        teacherToken = "Bearer " + teacherT.getToken();
    }

    @Test
    void getAllMark_ShouldReturnMarks_WhenAuthenticatedAsStudent() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-all-mark")
                        .header("Authorization", studentToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Successfully got all Students Marks."))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void getAllMark_ShouldReturnForbidden_WhenAuthenticatedAsTeacher() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-all-mark")
                        .header("Authorization", teacherToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllMarkBySubjectQuarter_ShouldReturnMarks_WhenAuthenticatedAsStudent() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-all-mark-subject/{subjectId}", subjectId)
                        .header("Authorization", studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got Marks. "));
    }

    @Test
    void getAvgMarkByGradeStudent_ShouldReturnAverageMarks_WhenAuthenticatedAsStudent() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-all-mark-for-year")
                        .header("Authorization", studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got Marks "));
    }

    @Test
    void getStudentSchedule_ShouldReturnSchedule_WhenAuthenticatedAsStudent() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-student-schedule")
                        .header("Authorization", studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got Schedules."))
                .andExpect(jsonPath("$.data[0].dayOfWeek").value("MONDAY"));
    }

    @Test
    void getAllLessonByGrade_ShouldReturnLessons_WhenAuthenticatedAsStudent() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-all-lesson-grade")
                        .header("Authorization", studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got Lessons."));
    }

    @Test
    void getAllMark_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/student/get-all-mark"))
                .andExpect(status().isFound());
    }
}