package com.example.demo;

import com.example.model.Light;
import com.example.model.Room;
import com.example.model.Status;
import com.example.repository.LightDao;
import com.example.repository.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController  // 1.
@RequestMapping("/api/rooms") // 2.
@Transactional // 3.
public class RoomController {

    @Autowired
    private LightDao lightDao; // 4.
    @Autowired
    private RoomDao roomDao;


    @GetMapping // 5.
    public List<RoomDto> findAll() {
        return roomDao.findAll()
                .stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public RoomDto findById(@PathVariable Integer id) {
        return roomDao.findById(id).map(room -> new RoomDto(room)).orElse(null);
    }

    @PutMapping(path = "/{id}/switch")
    public List<RoomDto> Switch(@PathVariable Integer id) {
        Room room = roomDao.findById(id).orElseThrow(IllegalArgumentException::new);
        for (Light light : room.getLights()){
            light.setStatus(light.getStatus() == Status.ON ? Status.OFF: Status.ON);
        }
        //return new RoomDto(room);
        return roomDao.findAll()
                .stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());

    }

/*    @PutMapping(path = "/{id}/{idl}/switchlight")
    public List<RoomDto> SwitchLightByRoom(@PathVariable Integer id) {
        Room room = roomDao.findById(id).orElseThrow(IllegalArgumentException::new);
        Light light=LightDao
        for (Light light : room.getLights()){
            light.setStatus(light.getStatus() == Status.ON ? Status.OFF: Status.ON);
        }
        //return new RoomDto(room);
        return roomDao.findAll()
                .stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());

    }
    @PutMapping(path = "/{id}/switch")
    public LightDto switchStatus(@PathVariable Integer id) {
        Light light = lightDao.findById(id).orElseThrow(IllegalArgumentException::new);
        light.setStatus(light.getStatus() == Status.ON ? Status.OFF: Status.ON);
        return new LightDto(light);
    }
*/
    @PostMapping
    public RoomDto create(@RequestBody RoomDto dto) {
        Room room = roomDao.save(new Room(dto.getName(), dto.getFloor()));
        return new RoomDto(room);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Integer id) {
        roomDao.deleteById(id);
    }
}

