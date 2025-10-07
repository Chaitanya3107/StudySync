package com.example.StudySync.focusroom.service;

import com.example.StudySync.focusroom.model.FocusRoom;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service

public class RoomService {
    private final Map<String, FocusRoom> roomMap = new ConcurrentHashMap<>();

    public FocusRoom getOrCreateRoom(String roomId) {
        // it checks for room in map for the specific roomId, if it is not present, it creates the room for the roomId and store it in map
        return roomMap.computeIfAbsent(roomId, value -> new FocusRoom());
    }

    public Set<String> getMembers(String roomId) {
        return getOrCreateRoom(roomId).getMembers();// this will take roomId and check for room in map, if its present, return its member, if not, create it
        // returns FocusRoom Object
    }

    public void addMember(String roomId, String username) {
        FocusRoom focusRoom = getOrCreateRoom(roomId);
        focusRoom.getMembers().add(username);// getting members list and adding new username in the list and in the room

    }

    public void removeMember(String roomId, String username) {
        FocusRoom focusRoom = roomMap.get(roomId);// if room is created
        if (focusRoom != null) {
            focusRoom.getMembers().remove(username);
        }

    }
}

