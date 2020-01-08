package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Room {
    @Id// 2.
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;

    @NotNull
    private Integer floor;
    @OneToMany(mappedBy = "room")
    @JsonManagedReference
    private List<Light> lights;

    public Room( String name, Integer floor) {
        this.name = name;
        this.floor=floor;
    }
    public Room() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }
}
