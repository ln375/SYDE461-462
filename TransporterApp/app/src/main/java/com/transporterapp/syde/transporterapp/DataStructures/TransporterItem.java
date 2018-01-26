package com.transporterapp.syde.transporterapp.DataStructures;

/**
 * Created by chari on 2018-01-25.
 */

public class TransporterItem {

    public final String id;
    public final String firstName;
    public final String lastName;
    public final String phoneNumber;
    public final String routeId;

    public TransporterItem(String id, String firstName, String lastName, String phoneNumber, String routeId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.routeId = routeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getId(){
        return id;
    }

}
