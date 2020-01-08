package com.example.repository;

import com.example.model.Light;

import java.util.List;

public interface RoomDaoCustom {
    List<Light> findOnLightsInRoom(Integer ID);
}
