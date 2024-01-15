package com.sobad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReadDto {
    private Long id;
    private String name;
    private Long customerId;
    private String customerName;
}
