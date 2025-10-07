package com.example.StudySync.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
// this is what our output will look like
@Data
@AllArgsConstructor
public class SubjectResponse {

    private int id;
    private String name;
    private String description;

}
