package com.sobad.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Project {
    private Long projectId;
    private String projectName;
    private Customer customer;
}
