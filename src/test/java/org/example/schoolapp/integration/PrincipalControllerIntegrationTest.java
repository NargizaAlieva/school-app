package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.request.*;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.role.PrincipleService;
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
public class PrincipalControllerIntegrationTest {

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

    private String principalToken;
    private Long scheduleId;
    private Long employeeId;
    private Long gradeId;
    private Long subjectId;
    private Long teacherUserId;

    @Autowired
    private PrincipleService principleService;

    @BeforeEach
    void setUp() {
        Role principalRole = roleRepository.findByTitle("PRINCIPAL").orElseThrow();

        User principalUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("principal@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> principalRoles = new HashSet<>();
        principalRoles.add(principalRole);
        principalUser.setRoleSet(principalRoles);
        entityManager.persist(principalUser);

        User teacherUser = User.builder()
                .firstName("Nana")
                .lastName("Mara")
                .email("teacher@school.com")
                .roleSet(new HashSet<>())
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(teacherUser);
        teacherUserId = teacherUser.getId();

        Employee employee = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(teacherUser)
                .build();
        entityManager.persist(employee);
        employeeId = employee.getId();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .classTeacher(employee)
                .isActive(true)
                .build();
        entityManager.persist(grade);
        gradeId = grade.getId();

        Subject subject = Subject.builder()
                .title("Mathematics")
                .description("Study of numbers, quantities, and shapes")
                .isActive(true)
                .build();
        entityManager.persist(subject);
        subjectId = subject.getId();

        Schedule schedule = Schedule.builder()
                .dayOfWeek(DaysOfWeek.MONDAY)
                .quarter(1)
                .dueTime("09:00-10:00")
                .schoolYear("2023-2024")
                .isApprove(false)
                .isActive(true)
                .subjectSchedule(subject)
                .teacherSchedule(employee)
                .gradeSchedule(grade)
                .build();
        entityManager.persist(schedule);
        scheduleId = schedule.getId();

        entityManager.flush();

        Token principalT = Token.builder()
                .token(jwtTokenUtil.generateToken(principalUser))
                .tokenType(TokenType.BEARER)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.getJwtExpiration()))
                .build();

        entityManager.persist(principalT);

        principalToken = "Bearer " + principalT.getToken();
    }

    @Test
    void getScheduleById_ShouldReturnSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-schedule/{scheduleId}", scheduleId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got Schedule with id"));
    }

    @Test
    void approveSchedule_ShouldApproveSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(put("/api/v1/principal/approve-schedule/{scheduleId}", scheduleId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully approved Schedule with id"));
    }

    @Test
    void disapproveSchedule_ShouldDisapproveSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(put("/api/v1/principal/disapprove-schedule/{scheduleId}", scheduleId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully disapproved Schedule with id"));
    }

    @Test
    void deleteSchedule_ShouldDeleteSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(delete("/api/v1/principal/delete-schedule/{scheduleId}", scheduleId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted Schedule with id"));
    }

    @Test
    void restoreSchedule_ShouldRestoreSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        principleService.deleteSchedule(scheduleId);

        mockMvc.perform(put("/api/v1/principal/restore-schedule/{scheduleId}", scheduleId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restored Schedule with id"));
    }

    @Test
    void getAllSchedule_ShouldReturnAllSchedules_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-schedule")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Schedules"));
    }

    @Test
    void getAllActiveSchedule_ShouldReturnActiveSchedules_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-active-schedule")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Schedules"));
    }

    @Test
    void getAllActiveScheduleByYear_ShouldReturnSchedules_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-active-schedule-by-year/{year}", "2023-2024")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Schedules by 2023-2024"));
    }

    @Test
    void getAllUnapprovedSchedule_ShouldReturnUnapprovedSchedules_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-unapproved-schedule")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all unapproved Schedules"));
    }

    @Test
    void createSchedule_ShouldCreateSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        ScheduleDtoRequest request = new ScheduleDtoRequest();
        request.setDayOfWeek("TUESDAY");
        request.setDueTime("10:00-11:00");
        request.setSubjectId(subjectId);
        request.setTeacherId(employeeId);
        request.setGradeId(gradeId);
        request.setQuarter(1);
        request.setSchoolYear("2023-2024");

        mockMvc.perform(post("/api/v1/principal/create-schedule")
                        .header("Authorization", principalToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Schedule."));
    }

    @Test
    void updateSchedule_ShouldUpdateSchedule_WhenAuthenticatedAsPrincipal() throws Exception {
        ScheduleDtoRequest request = new ScheduleDtoRequest();
        request.setId(scheduleId);
        request.setDayOfWeek("TUESDAY");
        request.setDueTime("10:00-11:00");
        request.setSubjectId(subjectId);
        request.setTeacherId(employeeId);
        request.setGradeId(gradeId);
        request.setQuarter(1);
        request.setSchoolYear("2023-2024");

        mockMvc.perform(put("/api/v1/principal/update-schedule")
                        .header("Authorization", principalToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Schedule successfully."));
    }

    @Test
    void updateEmployee_ShouldUpdateEmployee_WhenAuthenticatedAsPrincipal() throws Exception {
        EmployeeDroRequest request = new EmployeeDroRequest();
        request.setUserId(teacherUserId);
        request.setId(employeeId);
        request.setSalary(55000);

        mockMvc.perform(put("/api/v1/principal/update-employee")
                        .header("Authorization", principalToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Employee successfully."));
    }

    @Test
    void getAllActiveEmployee_ShouldReturnActiveEmployees_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-active-employee")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Employee."));
    }

    @Test
    void fireEmployee_ShouldDeleteEmployee_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(delete("/api/v1/principal/fire-employee/{employeeId}", employeeId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted successfully"));
    }

    @Test
    void createSubject_ShouldCreateSubject_WhenAuthenticatedAsPrincipal() throws Exception {
        SubjectDtoRequest request = new SubjectDtoRequest();
        request.setTitle("Physics");
        request.setDescription("Study of matter and energy");

        mockMvc.perform(post("/api/v1/principal/create-subject")
                        .header("Authorization", principalToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully added Subject."));
    }

    @Test
    void updateSubject_ShouldUpdateSubject_WhenAuthenticatedAsPrincipal() throws Exception {
        SubjectDtoRequest request = new SubjectDtoRequest();
        request.setTitle("Music");
        request.setId(subjectId);
        request.setDescription("Updated description");

        mockMvc.perform(put("/api/v1/principal/update-subject")
                        .header("Authorization", principalToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Subject successfully."));
    }

    @Test
    void deleteSubject_ShouldDeleteSubject_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(delete("/api/v1/principal/delete-subject/{id}", subjectId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted Subject successfully."));
    }

    @Test
    void restoreSubject_ShouldRestoreSubject_WhenAuthenticatedAsPrincipal() throws Exception {
        principleService.deleteSubject(subjectId);

        mockMvc.perform(put("/api/v1/principal/restore-subject/{subjectId}", subjectId)
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Restored Subject successfully."));
    }

    @Test
    void getAllSubject_ShouldReturnAllSubjects_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-subject")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Subject."));
    }

    @Test
    void getAllActiveSubject_ShouldReturnActiveSubjects_WhenAuthenticatedAsPrincipal() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-active-subject")
                        .header("Authorization", principalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Subject."));
    }

    @Test
    void getAllSchedule_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/principal/get-all-schedule"))
                .andExpect(status().isFound());
    }
}
