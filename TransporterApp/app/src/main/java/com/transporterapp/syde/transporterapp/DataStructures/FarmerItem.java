package com.transporterapp.syde.transporterapp.DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample firstName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FarmerItem {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<farmer> ITEMS = new ArrayList<farmer>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, farmer> ITEM_MAP = new HashMap<String, farmer>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createFarmerItem(i));
        }*/
    }

    private static void addItem(farmer item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static farmer createFarmerItem(String id, String firstName, String lastName, String phoneNumber, String routeId) {
        return new farmer(id, firstName, lastName, phoneNumber, routeId);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of firstName.
     */
    public static class farmer {
        public final String id;
        public final String firstName;
        public final String lastName;
        public final String phoneNumber;
        public final String routeId;

        public farmer(String id, String firstName, String lastName, String phoneNumber, String routeId) {
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
}
