package com.example.StudySync.focusroom.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusRoom {
    private String roomId;
    private Set<String> members = new HashSet<>();
    private TimerState timerState = TimerState.STOPPED;
    private int remainingSeconds = 0;

    // Getters and Setters
}

