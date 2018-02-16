package com.transporterapp.syde.transporterapp.DataStructures;

/**
 * Created by chari on 2018-01-25.
 */

public class Jug {

    public final String id;
    public final String size;
    public final String type;
    public final String transporterId;
    public final String currentVolume;

    public Jug(String id, String size, String type, String transporterId, String currentVolume){
        this.id = id;
        this.size = size;
        this.transporterId = transporterId;
        this.type = type;
        this.currentVolume = currentVolume;
    }

    public String getId() {
        return id;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public String getCurrentVolume() { return currentVolume; }
}
