package com.example.StudySync.focusroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomEventDTO {
    private String type;// join / leave
    private String username;
    private String roomId;
}
