package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.request.LessonDtoRequest;
import org.example.schoolapp.dto.request.MarkDtoRequest;
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
@AutoConfigureMockMvc(addFilters = false)
public class TeacherControllerIntegrationTest {

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

    private String teacherToken;
    private String studentToken;
    private Long gradeId;
    private Long studentId;
    private Long scheduleId;
    private Long lessonId;

    @BeforeEach
    void setUp() {
        User parentUser = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(parentUser);

        Set<Role> teacherRole = new HashSet<>();
        teacherRole.add(roleRepository.findByTitle("TEACHER").orElseThrow());

        Set<Role> studentRole = new HashSet<>();
        studentRole.add(roleRepository.findByTitle("STUDENT").orElseThrow());

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

        User teacherUser = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("teacher@school.com")
                .password("password123")
                .isActive(true)
                .roleSet(teacherRole)
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

        User studentUser = User.builder()
                .firstName("Mark")
                .lastName("Smith")
                .email("student@school.com")
                .password("password123")
                .isActive(true)
                .roleSet(studentRole)
                .build();
        entityManager.persist(studentUser);

        Student student = Student.builder()
                .birthday(new Date())
                .parentStatus(ParentStatus.FATHER)
                .user(studentUser)
                .grade(grade)
                .parent(parent)
                .build();
        entityManager.persist(student);
        studentId = student.getId();

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
        scheduleId = schedule.getId();

        Lesson lesson = Lesson.builder()
                .topic("Algebra Basics")
                .homework("Complete exercises 1-10")
                .schedule(schedule)
                .build();
        entityManager.persist(lesson);
        lessonId = lesson.getId();

        Mark mark = Mark.builder()
                .mark(85)
                .studentMark(student)
                .lessonMark(lesson)
                .build();
        entityManager.persist(mark);

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
    void getTeacherSchedule_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/teacher/get-schedule"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLesson_ShouldCreateLesson_WhenAuthenticatedAsTeacher() throws Exception {
        LessonDtoRequest request = new LessonDtoRequest();
        request.setScheduleId(scheduleId);
        request.setTopic("Geometry Basics");
        request.setHomework("Complete exercises 1-5");

        mockMvc.perform(post("/api/v1/teacher/create-lesson")
                        .header("Authorization", teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Lesson."))
                .andExpect(jsonPath("$.data.topic").value("Geometry Basics"));
    }

    @Test
    void updateLesson_ShouldUpdateLesson_WhenAuthenticatedAsTeacher() throws Exception {
        LessonDtoRequest request = new LessonDtoRequest();
        request.setId(lessonId);
        request.setScheduleId(scheduleId);
        request.setTopic("Advanced Algebra");
        request.setHomework("Complete exercises 11-20");

        mockMvc.perform(put("/api/v1/teacher/update-lesson")
                        .header("Authorization", teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated Lesson."));
    }

    @Test
    void createMark_ShouldCreateMark_WhenAuthenticatedAsTeacher() throws Exception {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setStudentId(studentId);
        request.setLessonId(lessonId);
        request.setMark(90);

        mockMvc.perform(post("/api/v1/teacher/create-mark")
                        .header("Authorization", teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Mark."));
    }

    @Test
    void updateMark_ShouldUpdateMark_WhenAuthenticatedAsTeacher() throws Exception {
        MarkDtoRequest request = new MarkDtoRequest();
        request.setId(1L); // Assuming the mark has ID 1
        request.setStudentId(studentId);
        request.setLessonId(lessonId);
        request.setMark(88);

        mockMvc.perform(put("/api/v1/teacher/update-mark")
                        .header("Authorization", teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully updated Mark."));
    }
}