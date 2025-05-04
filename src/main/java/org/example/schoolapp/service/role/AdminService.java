package org.example.schoolapp.service.role;

import org.example.schoolapp.dto.UserRoleDto;
import org.example.schoolapp.dto.request.EmployeeDroRequest;
import org.example.schoolapp.dto.request.ParentDtoRequest;
import org.example.schoolapp.dto.request.StudentDtoRequest;
import org.example.schoolapp.dto.request.UserDtoRequest;
import org.example.schoolapp.dto.response.*;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllUser();

    List<UserDto> getAllActiveUser();

    List<UserDto> getAllUserByRole(String role);
    List<EmployeeDto> getAllEmployee();
    List<EmployeeDto> getAllActiveEmployee();
    List<ParentDto> getAllParent();

    List<ParentDto> getAllActiveParent();

    List<StudentDto> getAllStudent();
    List<StudentDto> getAllActiveStudent();

    UserDto createUser(UserDtoRequest userDtoRequest);
    EmployeeDto createEmployee(EmployeeDroRequest employeeDroRequest);
    StudentDto createStudent(StudentDtoRequest studentDtoRequest);
    ParentDto createParent(ParentDtoRequest parentDtoRequest);

    UserDto updateUser(UserDtoRequest userDtoRequest);
    EmployeeDto updateEmployee(EmployeeDroRequest employeeDroRequest);
    StudentDto updateStudent(StudentDtoRequest studentDtoRequest);
    ParentDto updateParent(ParentDtoRequest parentDtoRequest);

    UserDto restoreUser(Long id);

    void deleteUser(Long id);

    UserDto addRoleToUser(UserRoleDto roleDto);
    UserDto removeRoleFromUser(UserRoleDto roleDto);
}
