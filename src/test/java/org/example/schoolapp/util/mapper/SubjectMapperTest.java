package org.example.schoolapp.util.mapper;

import org.example.schoolapp.dto.request.SubjectDtoRequest;
import org.example.schoolapp.dto.response.SubjectDto;
import org.example.schoolapp.entity.Employee;
import org.example.schoolapp.entity.Subject;
import org.example.schoolapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SubjectMapperTest {

    private SubjectMapper subjectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subjectMapper = new SubjectMapper();
    }

    @Test
    void testToSubjectDto() {
        Subject subject = Subject.builder()
                .id(1L)
                .title("Math")
                .description("Algebra and Geometry")
                .isActive(true)
                .teachersSet(Set.of(createMockEmployee("John", "Doe", "")))
                .build();

        SubjectDto subjectDto = subjectMapper.toSubjectDto(subject);

        assertNotNull(subjectDto);
        assertEquals(subject.getId(), subjectDto.getId());
        assertEquals(subject.getTitle(), subjectDto.getTitle());
        assertEquals(subject.getDescription(), subjectDto.getDescription());
        assertEquals(1, subjectDto.getTeachersSet().size());
        assertTrue(subjectDto.getTeachersSet().contains("John Doe"));
    }

    @Test
    void testToSubjectEntity() {
        SubjectDtoRequest subjectDtoRequest = new SubjectDtoRequest();
        subjectDtoRequest.setId(2L);
        subjectDtoRequest.setTitle("Physics");
        subjectDtoRequest.setDescription("Mechanics and Thermodynamics");
        subjectDtoRequest.setIsActive(true);

        Subject subject = subjectMapper.toSubjectEntity(subjectDtoRequest);

        assertNotNull(subject);
        assertEquals(subjectDtoRequest.getId(), subject.getId());
        assertEquals(subjectDtoRequest.getTitle(), subject.getTitle());
        assertEquals(subjectDtoRequest.getDescription(), subject.getDescription());
        assertEquals(subjectDtoRequest.getIsActive(), subject.getIsActive());
    }

    @Test
    void testToSubjectDtoList() {
        Subject subject1 = Subject.builder().id(1L).title("Math").description("Algebra").isActive(true).build();
        Subject subject2 = Subject.builder().id(2L).title("Biology").description("Genetics").isActive(false).build();

        List<SubjectDto> subjectDtos = subjectMapper.toSubjectDtoList(List.of(subject1, subject2));

        assertEquals(2, subjectDtos.size());
        assertEquals("Math", subjectDtos.get(0).getTitle());
        assertEquals("Biology", subjectDtos.get(1).getTitle());
    }

    @Test
    void testToSubjectDtoSet() {
        Subject subject1 = Subject.builder().id(1L).title("Chemistry").description("Organic Chemistry").isActive(true).build();
        Subject subject2 = Subject.builder().id(2L).title("History").description("World History").isActive(false).build();

        Set<SubjectDto> subjectDtos = subjectMapper.toSubjectDtoSet(Set.of(subject1, subject2));

        assertEquals(2, subjectDtos.size());
        assertTrue(subjectDtos.stream().anyMatch(dto -> dto.getTitle().equals("Chemistry")));
        assertTrue(subjectDtos.stream().anyMatch(dto -> dto.getTitle().equals("History")));
    }

    private Employee createMockEmployee(String firstName, String lastName, String middleName) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .build();

        return Employee.builder().user(user).build();
    }
}

