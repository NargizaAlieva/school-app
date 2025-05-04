package org.example.schoolapp.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private LocalDateTime creationDate;
    private Boolean isActive;
    private Set<String> roleSet;
}