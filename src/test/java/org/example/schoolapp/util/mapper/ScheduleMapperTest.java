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

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setTitle("Mathematics");

        Grade grade = new Grade();
        grade.setId(2L);
        grade.setTitle("10th Grade");

        User teacherUser = new User();
        teacherUser.setFirstName("John");
        teacherUser.setLastName("Doe");

        Employee teacher = new Employee();
        teacher.setUser(teacherUser);

        schedule = Schedule.builder()
                .id(1L)
                .dayOfWeek(DaysOfWeek.MONDAY)
                .dueTime("10:30")
                .quarter(1)
                .schoolYear("2025")
                .subjectSchedule(subject)
                .gradeSchedule(grade)
                .teacherSchedule(teacher)
                .isApprove(true)
                .isActive(true)
                .build();

        scheduleDtoRequest = ScheduleDtoRequest.builder()
                .id(1L)
                .dayOfWeek("MONDAY")
                .dueTime("10:30")
                .quarter(1)
                .schoolYear("2025")
                .subjectId(1L)
                .gradeId(2L)
                .teacherId(3L)
                .isApprove(true)
                .build();
    }

    @Test
    void testToScheduleDto() {
        ScheduleDto scheduleDto = scheduleMapper.toScheduleDto(schedule);

        assertNotNull(scheduleDto);
        assertEquals(1L, scheduleDto.getId());
        assertEquals(DaysOfWeek.MONDAY, scheduleDto.getDayOfWeek());
        assertEquals("10:30", scheduleDto.getDueTime());
        assertEquals(1, scheduleDto.getQuarter());
        assertEquals("2025", scheduleDto.getSchoolYear());
        assertEquals("Mathematics", scheduleDto.getSubjectTitle());
        assertEquals("10th Grade", scheduleDto.getGradeName());
        assertEquals("JohnDoe", scheduleDto.getTeacherName());
        assertTrue(scheduleDto.getIsApprove());
        assertTrue(scheduleDto.getIsActive());
    }

    @Test
    void testToScheduleEntity() {
        Subject mockSubject = new Subject();
        mockSubject.setId(1L);
        when(subjectService.findByIdEntity(1L)).thenReturn(mockSubject);

        Grade mockGrade = new Grade();
        mockGrade.setId(2L);
        when(gradeService.getByIdEntity(2L)).thenReturn(mockGrade);

        Employee mockTeacher = new Employee();
        mockTeacher.setId(3L);
        when(employeeService.findByIdEntity(3L)).thenReturn(mockTeacher);

        Schedule mappedSchedule = scheduleMapper.toScheduleEntity(scheduleDtoRequest);

        assertNotNull(mappedSchedule);
        assertEquals(1L, mappedSchedule.getId());
        assertEquals(DaysOfWeek.MONDAY, mappedSchedule.getDayOfWeek());
        assertEquals("10:30", mappedSchedule.getDueTime());
        assertEquals(1, mappedSchedule.getQuarter());
        assertEquals("2025", mappedSchedule.getSchoolYear());
        assertEquals(1L, mappedSchedule.getSubjectSchedule().getId());
        assertEquals(2L, mappedSchedule.getGradeSchedule().getId());
        assertEquals(3L, mappedSchedule.getTeacherSchedule().getId());
        assertTrue(mappedSchedule.getIsApprove());

        verify(subjectService, times(1)).findByIdEntity(1L);
        verify(gradeService, times(1)).getByIdEntity(2L);
        verify(employeeService, times(1)).findByIdEntity(3L);
    }
}

