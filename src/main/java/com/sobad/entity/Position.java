package com.sobad.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Position {
    private Long positionId;
    private String positionName;
}
