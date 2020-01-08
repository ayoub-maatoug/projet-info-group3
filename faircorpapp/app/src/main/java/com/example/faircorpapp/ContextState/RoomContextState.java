package com.example.faircorpapp.ContextState;

import org.json.JSONArray;

public class RoomContextState {

    private int roomId;
    private String roomName;
    private int roomFloor;
    private JSONArray lights;

    public RoomContextState(int roomId, String roomName, int roomFloor, JSONArray lights) {

        this.roomId = roomId;
        this.roomName = roomName;
        this.roomFloor = roomFloor;
        this.lights = lights;

    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomFloor() {
        return roomFloor;
    }

    public JSONArray getLights() {
        return lights;
    }
}

