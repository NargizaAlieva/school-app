package org.example.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    @NotNull(message = "Id cannot be null")
    private Long userId;

    @NotNull(message = "RoleId set cannot be null")
    private Set<Long> roleIdSet;
}
