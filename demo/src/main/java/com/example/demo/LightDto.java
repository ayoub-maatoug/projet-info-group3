package com.example.demo;

import com.example.model.Light;
import com.example.model.Room;
import com.example.model.Status;

public class LightDto {

    private  Integer id;
    private  Integer level;
    private Status status;
    private Room room;

    public LightDto() {
    }

    public LightDto(Light light) {
        this.id = light.getId();
        this.level = light.getLevel();
        this.status = light.getStatus();
        this.room = light.getRoom();

    }

    public Integer getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public Status getStatus() {
        return status;
    }


    public Integer getRoomId() {
        return room.getId();
    }
}