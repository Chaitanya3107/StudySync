package com.example.StudySync.plan.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank
    private String title;
    private String notes;
    private LocalDate dueDate;
}
