package com.example.faircorpapp.ContextState;


//Cette classe devait permettre de traiter les lumière mais on a préféré directement utiliser le JSON obtenu après le get pour traiter les informations.
// Elle n'est donc pas necessaire au projet
public class LightContextState {

    private int lightId;
    private int lightLevel;
    private String lightStatus;
    private int roomId;

    public LightContextState(int lightId, int lightLevel, String lightStatus, int roomId) {

        this.lightId = lightId;
        this.lightLevel = lightLevel;
        this.lightStatus = lightStatus;
        this.roomId = roomId;

    }

    public int getLightId() {
        return lightId;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public String getLightStatus() {
        return lightStatus;
    }

    public int getRoomId() {
        return roomId;
    }
}
