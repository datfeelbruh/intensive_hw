package com.sobad.entity;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Customer {
    private Long id;
    private String name;
    private List<Project> project;
}
