package org.example.schoolapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.schoolapp.config.JWTService;
import org.example.schoolapp.dto.*;
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.response.RoleDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.enums.TokenType;
import org.example.schoolapp.repository.RoleRepository;
import org.example.schoolapp.repository.UserRepository;
import org.example.schoolapp.service.role.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

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

    private String adminToken;
    private Long userId;
    private Long employeeId;
    private Long studentId;
    private Long parentId;
    private Long roleId;
    private Long gradeId;

    @Autowired
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        Role adminRole = roleRepository.findByTitle("ADMIN").orElseThrow();

        roleId = adminRole.getId();

        User adminUser = User.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin@school.com")
                .password("password123")
                .isActive(true)
                .build();
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRoleSet(adminRoles);
        entityManager.persist(adminUser);
        userId = adminUser.getId();

        User employeeUser = User.builder()
                .firstName("Regular")
                .lastName("User")
                .email("user@school.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(employeeUser);

        Employee employee = Employee.builder()
                .position("Teacher")
                .salary(50000)
                .user(employeeUser)
                .build();
        entityManager.persist(employee);
        employeeId = employee.getId();

        User parentUser = User.builder()
                .firstName("Regular")
                .lastName("User")
                .email("user123@school.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(parentUser);

        Parent parent = Parent.builder()
                .user(parentUser)
                .build();
        entityManager.persist(parent);
        parentId = parent.getId();

        Grade grade = Grade.builder()
                .title("Grade 10A")
                .classTeacher(employee)
                .isActive(true)
                .build();
        entityManager.persist(grade);
        gradeId = grade.getId();

        User studentUser = User.builder()
                .firstName("Regular")
                .lastName("User")
                .email("user12345@school.com")
                .password("password123")
                .isActive(true)
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

        entityManager.flush();

        Token adminT = Token.builder()
                .token(jwtTokenUtil.generateToken(adminUser))
                .tokenType(TokenType.BEARER)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.getJwtExpiration()))
                .build();

        entityManager.persist(adminT);

        adminToken = "Bearer " + adminT.getToken();
    }

    @Test
    void getAllUser_ShouldReturnUsers_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-user")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Users."));
    }

    @Test
    void getAllActiveUser_ShouldReturnActiveUsers_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-active-user")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Users."));
    }

    @Test
    void getAllUserByRole_ShouldReturnUsers_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-user-by-role/{role}", "ADMIN")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Users with Role.ADMIN"));
    }

    @Test
    void createUser_ShouldCreateUser_WhenAuthenticatedAsAdmin() throws Exception {
        UserDtoRequest request = new UserDtoRequest();
        request.setFirstName("New");
        request.setLastName("User");
        request.setEmail("new.user@school.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/v1/admin/create-user")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created User."))
                .andExpect(jsonPath("$.data.firstName").value("New"));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenAuthenticatedAsAdmin() throws Exception {
        UserDtoRequest request = new UserDtoRequest();
        request.setId(userId);
        request.setFirstName("Updated");
        request.setLastName("User");

        mockMvc.perform(put("/api/v1/admin/update-user")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated User successfully."));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/delete-user/{userId}", userId)
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted User successfully."));
    }

    @Test
    void restoreUser_ShouldRestoreUser_WhenAuthenticatedAsAdmin() throws Exception {
        // First delete the user
        adminService.deleteUser(userId);

        mockMvc.perform(put("/api/v1/admin/restore-user/{userId}", userId)
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Restored User successfully."));
    }

    @Test
    void createEmployee_ShouldCreateEmployee_WhenAuthenticatedAsAdmin() throws Exception {
        EmployeeDroRequest request = new EmployeeDroRequest();
        request.setUserId(1L);
        request.setPosition("Teacher");
        request.setSalary(45000);

        mockMvc.perform(post("/api/v1/admin/create-employee")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Employee."));
    }

    @Test
    void updateEmployee_ShouldUpdateEmployee_WhenAuthenticatedAsAdmin() throws Exception {
        EmployeeDroRequest request = new EmployeeDroRequest();
        request.setId(employeeId);
        request.setUserId(3L);
        request.setSalary(55000);

        mockMvc.perform(put("/api/v1/admin/update-employee")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Employee successfully."));
    }

    // Student Management Tests
    @Test
    void getAllStudent_ShouldReturnStudents_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-student")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Students."));
    }

    @Test
    void getAllActiveStudent_ShouldReturnActiveStudents_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-active-student")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Students."));
    }

    @Test
    void createStudent_ShouldCreateStudent_WhenAuthenticatedAsAdmin() throws Exception {
        StudentDtoRequest request = new StudentDtoRequest();
        request.setUserId(2L);
        request.setParentId(parentId);
        request.setGradeId(gradeId);
        request.setParentStatus("MOTHER");

        mockMvc.perform(post("/api/v1/admin/create-student")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Student."));
    }

    @Test
    void updateStudent_ShouldUpdateStudent_WhenAuthenticatedAsAdmin() throws Exception {
        StudentDtoRequest request = new StudentDtoRequest();
        request.setId(studentId);
        request.setUserId(2L);
        request.setGradeId(gradeId);
        request.setParentId(parentId);
        request.setParentStatus("MOTHER");

        mockMvc.perform(put("/api/v1/admin/update-student")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Student successfully."));
    }

    @Test
    void getAllParent_ShouldReturnParents_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-parent")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all Parents."));
    }

    @Test
    void getAllActiveParent_ShouldReturnActiveParents_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-active-parent")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully got all active Parents."));
    }

    @Test
    void createParent_ShouldCreateParent_WhenAuthenticatedAsAdmin() throws Exception {
        ParentDtoRequest request = new ParentDtoRequest();
        request.setUserId(3L);

        mockMvc.perform(post("/api/v1/admin/create-parent")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully added created Parent."));
    }

    @Test
    void updateParent_ShouldUpdateParent_WhenAuthenticatedAsAdmin() throws Exception {
        ParentDtoRequest request = new ParentDtoRequest();
        request.setId(parentId);
        request.setUserId(1L);

        mockMvc.perform(put("/api/v1/admin/update-parent")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated Parent successfully."));
    }

    // Role Management Tests
    @Test
    void getRoleById_ShouldReturnRole_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-role-by-id/{roleId}", roleId)
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved Role with Id: " + roleId));
    }

    @Test
    void getAllRoles_ShouldReturnRoles_WhenAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-role")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully retrieved all Roles."));
    }

    @Test
    void createRole_ShouldCreateRole_WhenAuthenticatedAsAdmin() throws Exception {
        RoleDto request = new RoleDto();
        request.setTitle("NEW_ROLE");

        mockMvc.perform(post("/api/v1/admin/create-role")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created Role."));
    }

    @Test
    void removeRoleFromUser_ShouldRemoveRole_WhenAuthenticatedAsAdmin() throws Exception {
        UserRoleDto request = new UserRoleDto();
        request.setUserId(userId);
        request.setRoleIdSet(Collections.singleton(roleId));

        mockMvc.perform(put("/api/v1/admin/remove-role")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Removed Role successfully"));
    }

    // Security Tests
    @Test
    void getAllUser_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/admin/get-all-user"))
                .andExpect(status().isFound());
    }

    @Test
    void createUser_ShouldReturnForbidden_WhenAuthenticatedAsNonAdmin() throws Exception {
        // Create a non-admin user
        User regularUser = User.builder()
                .firstName("Regular")
                .lastName("User")
                .email("regular.user@school.com")
                .password("password123")
                .isActive(true)
                .build();
        entityManager.persist(regularUser);
        String regularToken = "Bearer " + jwtTokenUtil.generateToken(regularUser);

        UserDtoRequest request = new UserDtoRequest();
        request.setFirstName("Test");
        request.setLastName("User");

        mockMvc.perform(post("/api/v1/admin/create-user")
                        .header("Authorization", regularToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isFound());
    }
}