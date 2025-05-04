package org.example.schoolapp.bootstrup;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.AuthProvider;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.enums.ParentStatus;
import org.example.schoolapp.service.entity.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitData {
    private final UserService userService;
    private final RoleService roleService;
    private final SubjectService subjectService;
    private final EmployeeService employeeService;
    private final GradeService gradeService;
    private final ParentService parentService;
    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final LessonService lessonService;
    private final MarkService markService;
    private final PasswordEncoder passwordEncoder;

    List<Subject> subjects = new ArrayList<>();
    List<Role> roles = new ArrayList<>();
    List<Employee> employees = new ArrayList<>();
    List<Grade> grades = new ArrayList<>();
    List<Parent> parents = new ArrayList<>();
    List<Student> students = new ArrayList<>();
    List<Schedule> schedules = new ArrayList<>();
    List<Lesson> lessons = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.warn("Init data");

        createUsers();
        createRoles();
        createSubjects();
        createEmployees();
        createGrades();
        createParents();
        createStudents();
        createSchedule();
        createLessons();
        createMarks();
    }

    private void createUsers() {
        User user1 = User.builder()
                .firstName("Alex")
                .lastName("Smith")
                .middleName("Robert")
                .phone("+1122334455")
                .email("alex@example.com")
                .password(passwordEncoder.encode("strongPassword789"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .build();

        User user2 = User.builder()
                .firstName("Emily")
                .lastName("Williams")
                .middleName("Grace")
                .phone("+1444555666")
                .email("emily.williams@example.com")
                .password(passwordEncoder.encode("EmilySecure789"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .build();

        User user3 = User.builder()
                .firstName("Daniel")
                .lastName("Brown")
                .middleName("Edward")
                .phone("+1555666777")
                .email("daniel.brown@example.com")
                .password(passwordEncoder.encode("DBrownPass321"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .build();

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
    }

    private void createRoles() {
        Role role1 = Role.builder()
                .title("PRINCIPAL")
                .build();

        Role role2 = Role.builder()
                .title("ADMIN")
                .build();

        Role role3 = Role.builder()
                .title("CLASS_TEACHER")
                .build();

        Role role4 = Role.builder()
                .title("USER")
                .build();

        Role role5 = Role.builder()
                .title("PARENT")
                .build();

        Role role6 = Role.builder()
                .title("STUDENT")
                .build();

        Role role7 = Role.builder()
                .title("TEACHER")
                .build();

        roleService.save(role1);
        roleService.save(role2);
        roleService.save(role3);
        roleService.save(role4);
        roleService.save(role5);
        roleService.save(role6);
        roleService.save(role7);

        roles.add(role1);
        roles.add(role2);
        roles.add(role3);
        roles.add(role4);
        roles.add(role5);
        roles.add(role6);
        roles.add(role7);
    }

    private void createSubjects() {
        Subject subject1 = Subject.builder()
                .title("Maths")
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

        subjectService.save(subject1);
        subjectService.save(subject2);
        subjectService.save(subject3);

        subjects.add(subject1);
        subjects.add(subject2);
        subjects.add(subject3);
    }

    private void createEmployees() {
        Set<Role> roleset = new HashSet<>();
        roleset.add(roles.get(6));
        roleset.add(roles.get(2));

        User user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .phone("+1234567890")
                .email("john@example.com")
                .password(passwordEncoder.encode("password123"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        User user2 = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .middleName("Elizabeth")
                .phone("+9876543210")
                .email("jane@example.com")
                .password(passwordEncoder.encode("securePass456"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        roleset = new HashSet<>();
        roleset.add(roles.get(1));
        User user3 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .phone("+1234567890")
                .email("jonatan@example.com")
                .password(passwordEncoder.encode("password123"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        roleset = new HashSet<>();
        roleset.add(roles.get(0));
        User user4 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .phone("+1234567890")
                .email("jenny@example.com")
                .password(passwordEncoder.encode("securePass456"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        Set<Subject> subjectsFor1 = new HashSet<>();
        subjectsFor1.add(subjects.get(0));
        subjectsFor1.add(subjects.get(2));

        Set<Subject> subjectsFor2 = new HashSet<>();
        subjectsFor2.add(subjects.get(1));

        Employee employee1 = Employee.builder()
                .position("Math Teacher")
                .salary(50000)
                .user(user1)
                .subjectSet(subjectsFor1)
                .build();

        Employee employee2 = Employee.builder()
                .position("History Teacher")
                .salary(50000)
                .user(user2)
                .subjectSet(subjectsFor2)
                .build();

        Employee employee3 = Employee.builder()
                .position("Principal")
                .salary(100000)
                .user(user4)
                .build();

        Employee employee4 = Employee.builder()
                .position("Admin")
                .salary(80000)
                .user(user3)
                .build();

        employeeService.save(employee1);
        employees.add(employee1);

        employeeService.save(employee2);
        employees.add(employee2);

        employeeService.save(employee3);
        employees.add(employee3);

        employeeService.save(employee4);
        employees.add(employee4);
    }

    private void createGrades() {
        Grade grade1 = Grade.builder()
                .title("10B")
                .classTeacher(employees.get(0))
                .build();

        Grade grade2 = Grade.builder()
                .title("10A")
                .classTeacher(employees.get(1))
                .build();

        Grade grade3 = Grade.builder()
                .title("11A")
                .classTeacher(employees.get(1))
                .isActive(false)
                .build();

        gradeService.save(grade1);
        gradeService.save(grade2);
        gradeService.save(grade3);

        grades.add(grade1);
        grades.add(grade2);
        grades.add(grade3);
    }

    private void createParents() {
        Set<Role> roleset = new HashSet<>();
        roleset.add(roles.get(4));

        User user1 = User.builder()
                .firstName("Emily")
                .lastName("Williams")
                .middleName("Anne")
                .phone("+1987654321")
                .email("melly@example.com")
                .password(passwordEncoder.encode("securePass456"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        User user2 = User.builder()
                .firstName("David")
                .lastName("Smith")
                .middleName("James")
                .phone("+1765432109")
                .email("eric@example.com")
                .password(passwordEncoder.encode("strongPass789"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        Parent parent1 = Parent.builder()
                .user(user1)
                .build();

        Parent parent2 = Parent.builder()
                .user(user2)
                .build();

        parentService.save(parent1);
        parentService.save(parent2);

        parents.add(parent1);
        parents.add(parent2);
    }

    private void createStudents() {
        Set<Role> roleset = new HashSet<>();
        roleset.add(roles.get(5));

        User user1 = User.builder()
                .firstName("Sarah")
                .lastName("Miller")
                .middleName("Elizabeth")
                .phone("+1654321098")
                .email("sarah@example.com")
                .password(passwordEncoder.encode("myPassword321"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        User user2 = User.builder()
                .firstName("Michael")
                .lastName("Brown")
                .middleName("William")
                .phone("+1543210987")
                .email("michael@example.com")
                .password(passwordEncoder.encode("pass987654"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        User user3 = User.builder()
                .firstName("Niko")
                .lastName("Brown")
                .middleName("William")
                .phone("+1543090987")
                .email("niko@example.com")
                .password(passwordEncoder.encode("pass987654"))
                .isEnabled(true)
                .provider(AuthProvider.LOCAL)
                .roleSet(roleset)
                .build();

        Student student1 = Student.builder()
                .user(user1)
                .grade(grades.get(0))
                .parent(parents.get(0))
                .parentStatus(ParentStatus.MOTHER)
                .birthday(new Date(2005-10-10))
                .build();

        Student student2 = Student.builder()
                .user(user2)
                .grade(grades.get(0))
                .parent(parents.get(1))
                .parentStatus(ParentStatus.FATHER)
                .birthday(new Date(2005-8-8))
                .build();

        Student student3 = Student.builder()
                .user(user3)
                .grade(grades.get(1))
                .parent(parents.get(1))
                .parentStatus(ParentStatus.FATHER)
                .birthday(new Date(2005-11-11))
                .build();

        studentService.save(student1);
        studentService.save(student2);
        studentService.save(student3);

        students.add(student1);
        students.add(student2);
        students.add(student3);
    }

    private void createSchedule() {
        Schedule schedule1 = Schedule.builder()
                .teacherSchedule(employees.get(0))
                .gradeSchedule(grades.get(0))
                .subjectSchedule(subjects.get(1))
                .quarter(1)
                .dueTime("10:30-11:15")
                .dayOfWeek(DaysOfWeek.MONDAY)
                .schoolYear("2024-2025")
                .isActive(true)
                .build();

        Schedule schedule2 = Schedule.builder()
                .teacherSchedule(employees.get(1))
                .gradeSchedule(grades.get(1))
                .subjectSchedule(subjects.get(1))
                .quarter(1)
                .dueTime("10:30-11:15")
                .dayOfWeek(DaysOfWeek.MONDAY)
                .schoolYear("2024-2025")
                .build();

        Schedule schedule3 = Schedule.builder()
                .teacherSchedule(employees.get(0))
                .gradeSchedule(grades.get(0))
                .subjectSchedule(subjects.get(1))
                .quarter(1)
                .dueTime("11:15-12:00")
                .dayOfWeek(DaysOfWeek.MONDAY)
                .schoolYear("2024-2025")
                .build();

        scheduleService.save(schedule1);
        scheduleService.save(schedule2);
        scheduleService.save(schedule3);

        schedules.add(schedule1);
        schedules.add(schedule2);
        schedules.add(schedule3);
    }

    private void createLessons() {
        Lesson lesson1 = Lesson.builder()
                .schedule(schedules.get(0))
                .topic("Intro")
                .homework("Search")
                .build();

        Lesson lesson2 = Lesson.builder()
                .schedule(schedules.get(1))
                .topic("Intro")
                .homework("Search")
                .build();

        Lesson lesson3 = Lesson.builder()
                .schedule(schedules.get(2))
                .topic("Intro")
                .build();

        lessonService.save(lesson1);
        lessonService.save(lesson2);
        lessonService.save(lesson3);

        lessons.add(lesson1);
        lessons.add(lesson2);
        lessons.add(lesson3);
    }

    private void createMarks() {
        Mark mark1 = Mark.builder()
                .mark(100)
                .studentMark(students.get(0))
                .lessonMark(lessons.get(0))
                .build();

        Mark mark2 = Mark.builder()
                .mark(90)
                .studentMark(students.get(0))
                .lessonMark(lessons.get(0))
                .build();

        Mark mark3 = Mark.builder()
                .mark(80)
                .studentMark(students.get(0))
                .lessonMark(lessons.get(1))
                .build();

        Mark mark4 = Mark.builder()
                .mark(60)
                .studentMark(students.get(1))
                .lessonMark(lessons.get(0))
                .build();

        Mark mark5 = Mark.builder()
                .mark(75)
                .studentMark(students.get(2))
                .lessonMark(lessons.get(1))
                .build();

        Mark mark6 = Mark.builder()
                .mark(45)
                .studentMark(students.get(2))
                .lessonMark(lessons.get(0))
                .build();

        Mark mark7 = Mark.builder()
                .mark(70)
                .studentMark(students.get(1))
                .lessonMark(lessons.get(1))
                .build();

        markService.save(mark1);
        markService.save(mark2);
        markService.save(mark3);
        markService.save(mark4);
        markService.save(mark5);
        markService.save(mark6);
        markService.save(mark7);
    }
}
