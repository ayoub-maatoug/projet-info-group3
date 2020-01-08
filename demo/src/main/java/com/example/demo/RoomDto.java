package com.example.demo;

import com.example.model.Light;
import com.example.model.Room;

import java.util.List;

public class RoomDto {

    private  Integer id;
    private String name;
    private  Integer floor;
    private List<Light> lights;

    public RoomDto() {
    }

    public RoomDto(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.floor = room.getFloor();
        this.lights = room.getLights();

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getFloor() {
        return floor;
    }


    public List<Light> getLights() {
        return lights;
    }
}
