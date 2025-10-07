package com.example.StudySync.focusroom.service;

import com.example.StudySync.focusroom.dto.TimerUpdateDTO;
import com.example.StudySync.focusroom.model.FocusRoom;
import com.example.StudySync.focusroom.model.TimerState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class TimerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;
    private final Map<String, ScheduledFuture<?>> timers = new ConcurrentHashMap<>(); // hashmap to store timer details
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);  // Creates 1-thread scheduler to run tasks


    public void startFocusTimer(String roomId, int durationInMinutes) {
        stopTimer(roomId); // stop existing timer if any;
        FocusRoom focusRoom = roomService.getOrCreateRoom(roomId); // get the focus room in which timer need to be started
        focusRoom.setTimerState(TimerState.FOCUS);
        focusRoom.setRemainingSeconds(durationInMinutes); // currently in seconds
//        Starts a repeating task that Runs every second
//        Decreases the remaining time by 1 second  Sends an update to all users via broadcastUpdate()
//        If time is up, it automatically switches to a break timer
//        The timer keeps ticking every second using scheduleAtFixedRate.
        System.out.println("🟢 Timer started for : " + roomId);
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(()->{
            if(focusRoom.getRemainingSeconds()<=0){
                startBreakTimer(roomId); // switch to break
                return;
            }
            System.out.println("The remaining time is "+focusRoom.getRemainingSeconds());
            focusRoom.setRemainingSeconds(focusRoom.getRemainingSeconds()-1);
            broadcastUpdate(focusRoom);
        },0,1,TimeUnit.SECONDS);
        timers.put(roomId,future);
    }

    private void startBreakTimer(String roomId) {
        stopTimer(roomId);
        FocusRoom focusRoom = roomService.getOrCreateRoom(roomId);
        focusRoom.setTimerState(TimerState.BREAK);
        focusRoom.setRemainingSeconds(5); // break after 25 min timer

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(()->{
            if(focusRoom.getRemainingSeconds()<=0){
                stopTimer(roomId);
                return;
            }
            System.out.println("Break remaining time is "+focusRoom.getRemainingSeconds());
            focusRoom.setRemainingSeconds(focusRoom.getRemainingSeconds()-1);
            broadcastUpdate(focusRoom);
        },0,1,TimeUnit.SECONDS);
        timers.put(roomId,future);
    }

    public void stopTimer(String roomId) {
        if(timers.containsKey(roomId)){
            timers.get(roomId).cancel(true);// this gets future object and cancel the task which is running again and again
            timers.remove(roomId);
            System.out.println("Timer stopped!!");
            // Optionally notify clients
            TimerUpdateDTO update = new TimerUpdateDTO(roomId, "STOPPED", 0);
            messagingTemplate.convertAndSend("/topic/room/" + roomId + "/timer", update);
        }
    }


// this broadcast update to users using websockets who are subscribed to the url
    private void broadcastUpdate(FocusRoom focusRoom) {
        TimerUpdateDTO timerUpdateDTO = new TimerUpdateDTO();
        timerUpdateDTO.setRemainingSeconds(focusRoom.getRemainingSeconds());
        timerUpdateDTO.setRoomId(focusRoom.getRoomId());
        timerUpdateDTO.setState(focusRoom.getTimerState().toString());
        messagingTemplate.convertAndSend("/topic/room/"+focusRoom.getRoomId()+"/timer",timerUpdateDTO);
    }




}


















