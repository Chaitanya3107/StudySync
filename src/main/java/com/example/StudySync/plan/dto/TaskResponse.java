package com.example.StudySync.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
// this is what our output will look like
@Data
@AllArgsConstructor
public class TaskResponse {
    private Integer id;
    private String title;
    private String notes;
    private boolean isCompleted;
    private LocalDate dueDate;
}
