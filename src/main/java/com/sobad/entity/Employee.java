package com.sobad.entity;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private Project project;
    private Position position;
}
