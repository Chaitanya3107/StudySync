package com.example.StudySync.focusroom.controller;

import com.example.StudySync.focusroom.dto.RoomEventDTO;
import com.example.StudySync.focusroom.service.RoomService;
import com.example.StudySync.focusroom.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class FocusRoomController {
    private final RoomService roomService;
    private final TimerService timerService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        return "📢 " + message;
    }

    @MessageMapping("/room/{roomId}/join")
    public void joinRoom(@DestinationVariable String roomId, Principal principal){
        String username = principal.getName();
        roomService.addMember(roomId,username);
        RoomEventDTO eventDTO = new RoomEventDTO();
        eventDTO.setRoomId(roomId);
        eventDTO.setUsername(username);
        eventDTO.setType("join");
        System.out.println(username+" joined room");

        // broadcast messages to all the subscribed user to the /topic/--- url
        messagingTemplate.convertAndSend("/topic/room/"+roomId+"/events",eventDTO);
        // this functions take input destination url and the object which it is sending
    }

    @MessageMapping("room/{roomId}/start")
    public void startTimer(@DestinationVariable String roomId,Principal principal){
        System.out.println("Timer Started!!");
        timerService.startFocusTimer(roomId,25);

    }

    @MessageMapping("room/{roomId}/leave")
    public void leaveRoom(@DestinationVariable String roomId,Principal principal){
        String username = principal.getName();
        roomService.removeMember(roomId,username);
        System.out.println(username+" user left the room");
        RoomEventDTO eventDTO = new RoomEventDTO();
        eventDTO.setType("leave");
        eventDTO.setUsername(username);
        eventDTO.setRoomId(roomId);     // destination                             payload
        messagingTemplate.convertAndSend("/topic/room"+roomId+"/events",eventDTO);
    }

    @MessageMapping("/room/{roomId}/stop")
    public void stopTimer(@DestinationVariable String roomId, Principal principal) {
        timerService.stopTimer(roomId);
    }


}








