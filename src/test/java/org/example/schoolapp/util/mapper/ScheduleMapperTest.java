package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.ScheduleDtoRequest;
import org.example.schoolapp.dto.response.ScheduleDto;
import org.example.schoolapp.entity.*;
import org.example.schoolapp.enums.DaysOfWeek;
import org.example.schoolapp.service.EmployeeService;
import org.example.schoolapp.service.GradeService;
import org.example.schoolapp.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleMapperTest {

    @Mock
    private SubjectService subjectService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private ScheduleMapper scheduleMapper;

    private Schedule schedule;
    private ScheduleDtoRequest scheduleDtoRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Subject subject = Subject.builder()
                .id(1L)
                .title("Math")
                .description("Algebra and Geometry")
                .isActive(true)
                .build();

        User user = User.builder()
                .username("Sara Doe")
                .firstName("Sara")
                .lastName("Doe")
                .middleName("M.")
                .email("sara@gmail.com")
                .build();

        Employee teacher = Employee.builder()
                .id(1L)
                .user(user)
                .build();

        Grade grade = Grade.builder()
                .id(1L)
                .title("10A")
                .classTeacher(teacher)
                .studentSet(Set.of())
                .isActive(true)
                .build();

        schedule = Schedule.builder()
                .id(1L)
                .dayOfWeek(DaysOfWeek.MONDAY)
                .dueTime("10:30-11.15")
                .quarter(1)
                .schoolYear("2023-2024")
                .subjectSchedule(subject)
                .gradeSchedule(grade)
                .teacherSchedule(teacher)
                .isApprove(true)
                .isActive(true)
                .build();

        scheduleDtoRequest = ScheduleDtoRequest.builder()
                .id(1L)
                .dayOfWeek("MONDAY")
                .dueTime("10:30-11.15")
                .quarter(1)
                .schoolYear("2023-2024")
                .subjectId(1L)
                .gradeId(1L)
                .teacherId(1L)
                .isApprove(true)
                .build();
    }

    @Test
    void testToScheduleDto() {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(schedule);

        assertNotNull(scheduleDto);
        assertEquals(1L, scheduleDto.getId());
        assertEquals(DaysOfWeek.MONDAY, scheduleDto.getDayOfWeek());
        assertEquals("10:30-11.15", scheduleDto.getDueTime());
        assertEquals(1, scheduleDto.getQuarter());
        assertEquals("2023-2024", scheduleDto.getSchoolYear());
        assertEquals("Math", scheduleDto.getSubjectTitle());
        assertEquals("10A", scheduleDto.getGradeName());
        assertEquals("Sara Doe", scheduleDto.getTeacherName());
        assertTrue(scheduleDto.getIsApprove());
        assertTrue(scheduleDto.getIsActive());
    }

    @Test
    void testToScheduleEntity() {
        Subject mockSubject = new Subject();
        mockSubject.setId(1L);
        when(subjectService.findByIdEntity(1L)).thenReturn(mockSubject);

        Grade mockGrade = new Grade();
        mockGrade.setId(1L);
        when(gradeService.getByIdEntity(1L)).thenReturn(mockGrade);

        Employee mockTeacher = new Employee();
        mockTeacher.setId(1L);
        when(employeeService.findByIdEntity(1L)).thenReturn(mockTeacher);

        Schedule mappedSchedule = scheduleMapper.toScheduleEntity(scheduleDtoRequest);

        assertNotNull(mappedSchedule);
        assertEquals(1L, mappedSchedule.getId());
        assertEquals(DaysOfWeek.MONDAY, mappedSchedule.getDayOfWeek());
        assertEquals("10:30-11.15", mappedSchedule.getDueTime());
        assertEquals(1, mappedSchedule.getQuarter());
        assertEquals("2023-2024", mappedSchedule.getSchoolYear());
        assertEquals(1L, mappedSchedule.getSubjectSchedule().getId());
        assertEquals(1L, mappedSchedule.getGradeSchedule().getId());
        assertEquals(1L, mappedSchedule.getTeacherSchedule().getId());
        assertTrue(mappedSchedule.getIsApprove());

        verify(subjectService, times(1)).findByIdEntity(1L);
        verify(gradeService, times(1)).getByIdEntity(1L);
        verify(employeeService, times(1)).findByIdEntity(1L);
    }
}

