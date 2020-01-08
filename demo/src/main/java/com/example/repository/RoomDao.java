package com.example.repository;

import com.example.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao  extends JpaRepository<Room, Integer>, RoomDaoCustom {

}
