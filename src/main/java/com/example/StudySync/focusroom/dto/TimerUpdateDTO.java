package com.example.StudySync.focusroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimerUpdateDTO {
    private String roomId;
    private String state; // FOCUS or BREAK
    private int remainingSeconds;
}
