package com.transporterapp.syde.transporterapp.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

/**
 * Helper class for providing sample firstName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FarmerItem implements Parcelable {

    protected FarmerItem(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        routeId = in.readString();
    }

    public static final Creator<FarmerItem> CREATOR = new Creator<FarmerItem>() {
        @Override
        public FarmerItem createFromParcel(Parcel in) {
            return new FarmerItem(in);
        }

        @Override
        public FarmerItem[] newArray(int size) {
            return new FarmerItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(phoneNumber);
        parcel.writeString(routeId);
    }
}
