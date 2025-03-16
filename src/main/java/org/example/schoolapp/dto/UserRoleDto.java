package org.example.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    private Long userId;
    private Set<Long> roleIdSet;
}
