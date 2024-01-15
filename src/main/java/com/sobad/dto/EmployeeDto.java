package com.sobad.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private Long projectId;
    private Long positionId;
}
