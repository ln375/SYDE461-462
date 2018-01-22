package com.transporterapp.syde.transporterapp.DataStructures;

/**
 * Helper class for providing sample firstName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FarmerItem {

    public static FarmerItem createFarmerItem(String id, String firstName, String lastName, String phoneNumber, String routeId) {
        return new FarmerItem(id, firstName, lastName, phoneNumber, routeId);
    }

    public final String id;
    public final String firstName;
    public final String lastName;
    public final String phoneNumber;
    public final String routeId;

    public FarmerItem(String id, String firstName, String lastName, String phoneNumber, String routeId) {
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
