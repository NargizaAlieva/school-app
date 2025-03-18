package org.example.schoolapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.response.StudentDto;
import org.example.schoolapp.entity.Student;
import org.example.schoolapp.repository.StudentRepository;
import org.example.schoolapp.service.StudentService;
import org.example.schoolapp.service.UserService;
import org.example.schoolapp.util.exception.AlreadyExistException;
import org.example.schoolapp.util.exception.ObjectNotFoundException;
import org.example.schoolapp.util.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserService userService;

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentByIdEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Student with id: '" + id + "' not found"));
    }

    @Override
    public StudentDto getStudentById(Long id) {
        return studentMapper.toStudentDto(getStudentByIdEntity(id));
    }

    @Override
    public List<StudentDto> getAllStudent() {
        return studentMapper.toStudentDtoList(studentRepository.findAll());
    }

    @Override
    public List<StudentDto> getAllActiveStudent() {
        List<Student> activeStudents = studentRepository.findAllActiveStudents();
        return studentMapper.toStudentDtoList(activeStudents);
    }

    @Override
    public List<StudentDto> getStudentByParentId(Long parentId) {
        List<Student> students = studentRepository.findParentStudentsByParentId(parentId);
        return studentMapper.toStudentDtoList(students);
    }

    @Override
    public List<StudentDto> getAllStudentByGrade(Long gradeId) {
        List<Student> students = studentRepository.findAllActiveStudentsByGrade(gradeId);
        return studentMapper.toStudentDtoList(students);
    }

    @Override
    public StudentDto createStudent(StudentDtoRequest studentDtoRequest) {
        if((studentRepository.existsByUserId(studentDtoRequest.getUserId())))
            throw new AlreadyExistException("Student", "userId", studentDtoRequest.getGradeId());

        return studentMapper.toStudentDto(save(studentMapper.toStudentEntity(studentDtoRequest)));
    }

    @Override
    public StudentDto updateStudent(StudentDtoRequest studentDtoRequest) {
        if (userService.getById(studentDtoRequest.getUserId()) == null)
            throw new ObjectNotFoundException("User");

        Student request = studentMapper.toStudentEntity(studentDtoRequest);
        Student student = getStudentByIdEntity(studentDtoRequest.getId());

        if(!request.getUser().getId().equals(student.getUser().getId()))
            if (studentRepository.existsByUserId(studentDtoRequest.getUserId()))
                throw new AlreadyExistException("Student", "userId", studentDtoRequest.getUserId());

        if (request.getBirthday() != null)
            request.setBirthday(student.getBirthday());

        student = student.toBuilder()
                .id(request.getId())
                .birthday(request.getBirthday())
                .parentStatus(request.getParentStatus())
                .parent(request.getParent())
                .grade(request.getGrade())
                .user(request.getUser())
                .build();

        return studentMapper.toStudentDto(save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = getStudentByIdEntity(id);
        userService.deleteUser(getStudentByIdEntity(id).getUser().getId());
        studentMapper.toStudentDto(student);
    }

    @Override
    public StudentDto restoreStudent(Long id) {
        Student student = getStudentByIdEntity(id);
        userService.restoreUser(getStudentByIdEntity(id).getUser().getId());
        return studentMapper.toStudentDto(student);
    }
}
