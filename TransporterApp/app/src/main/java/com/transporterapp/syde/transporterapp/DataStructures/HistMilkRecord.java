package com.transporterapp.syde.transporterapp.DataStructures;

/**
 * Created by chari on 2018-01-21.
 */

public class HistMilkRecord {

    public final String id;
    public final String transporterId;
    public final String farmerId;
    public final String jugId;
    public final String date;
    public final String time;
    public final String milkWeight;
    public final String alcohol;
    public final String smell;
    public final String comments;
    public final String density;
    public final String trTransporterCoolingId;
    public final String routeId;
    public final String status;
    public final String trFarmerTransporterId;


    public HistMilkRecord(String id, String transporterId, String farmerId, String jugId, String date,
                          String time, String milkWeight, String alcohol, String smell, String comments,
                          String density, String trTransporterCoolingId, String routeId, String status,
                          String trFarmerTransporterId){
        this.id = id;
        this.transporterId = transporterId;
        this.farmerId = farmerId;
        this.jugId = jugId;
        this.date = date;
        this.time = time;
        this.milkWeight = milkWeight;
        this.alcohol = alcohol;
        this.smell = smell;
        this.comments = comments;
        this.density = density;
        this.trTransporterCoolingId = trTransporterCoolingId;
        this.routeId = routeId;
        this.status = status;
        this.trFarmerTransporterId = trFarmerTransporterId;
    }

    public static HistMilkRecord createMilkRecord(String id, String transporterId, String farmerId, String jugId, String date,
                                                  String time, String milkWeight, String alcohol, String smell, String comments,
                                                  String density, String trTransporterCoolingId, String routeId, String status,
                                                  String trFarmerTransporterId) {
        return new HistMilkRecord(id, transporterId, farmerId, jugId, date, time, milkWeight, alcohol,
                smell, comments, density, trTransporterCoolingId, routeId, status, trFarmerTransporterId);
    }

    public String getId() {
        return id;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public String getJugId() {
        return jugId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMilkWeight() {
        return milkWeight;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public String getSmell() {
        return smell;
    }

    public String getComments() {
        return comments;
    }

    public String getDensity() {
        return density;
    }

    public String getTrTransporterCoolingId() {
        return trTransporterCoolingId;
    }

    public String getRouteId(){
        return routeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        status = status;
    }

    public String getTrFarmerTransporterId() {
        return trFarmerTransporterId;
    }
}
