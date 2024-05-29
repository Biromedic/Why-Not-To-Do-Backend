package com.example.whynottodo.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    private long id;
    private String description;
    private boolean complete;
}
