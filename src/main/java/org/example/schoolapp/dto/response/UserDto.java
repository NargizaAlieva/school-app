package org.example.schoolapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Schema(description = "The unique identifier of the user", example = "1", required = true)
    @NotNull(message = "Id cannot be null")
    private Long id;

    @Schema(description = "The username of the user", example = "john_doe", required = true)
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(description = "The first name of the user", example = "John", required = true)
    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    private String firstName;

    @Schema(description = "The last name of the user", example = "Doe", required = true)
    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    private String lastName;

    @Schema(description = "The middle name of the user", example = "Michael", required = false)
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    private String middleName;

    @Schema(description = "The phone number of the user", example = "+1234567890", required = false)
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Phone number must be valid")
    private String phone;

    @Schema(description = "The email address of the user", example = "john.doe@example.com", required = true)
    @NotNull(message = "Email name cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "The date and time when the user was created", example = "2023-10-01T12:00:00", required = true)
    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime creationDate;

    @Schema(description = "Indicates whether the user is active", example = "true", required = true)
    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;

    @Schema(description = "The set of roles assigned to the user", example = "[\"ADMIN\", \"USER\"]", required = false)
    private Set<String> roleSet;
}