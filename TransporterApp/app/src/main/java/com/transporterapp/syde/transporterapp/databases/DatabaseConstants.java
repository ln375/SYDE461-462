package com.transporterapp.syde.transporterapp.databases;

/**
 * Created by chari on 2018-01-20.
 */

public class DatabaseConstants {

    public static final String tblFarmer = "farmers";

    public static final String[] colFarmer = new String[]{"id", "first_name", "last_name", "phone_number", "route_id"};

    public static final String tblJug = "jug";

    public static final String[] colJug = new String[]{"id", "size", "type", "transporter_id", "currentVolume"};

    public static final String tblRoute = "route";

    public static final String[] colRoute = new String[]{"id", "route"};

    public static final String tbltrFarmerTransporter = "tr_farmer_transporter";

    public static final String tblHisttrFarmerTransporter = "hist_tr_farmer_transporter";

    public static final String[] coltrFarmerTransporter = new String[]{"id", "transporter_id", "farmer_id", "jug_id", "date", "time",
            "milk_weight", "alcohol", "smell", "comments", "density", "tr_transporter_cooling_id", "route_id", "status"};

    public static final String[] colHistTrFarmerTransporter = new String[]{"id", "transporter_id", "farmer_id", "jug_id", "modified_date", "modified_time",
            "milk_weight", "alcohol", "smell", "comments", "density", "tr_transporter_cooling_id", "route_id", "status", "tr_farmer_transporter_id"};

    public static final String tbltrTransporterCooling = "tr_transporter_cooling";

    public static final String[] coltrTransporterCooling = new String[]{"id", "transporter_id", "route_id", "jug_id", "date", "time",
            "enroute_weight", "measured_weight", "alcohol", "density", "smell", "comments", "tr_farmer_transporter_id"};

    public static final String tblTransporter = "transporter";

    public static final String[] colTransporter = new String[]{"id", "first_name", "last_name", "phone_number", "route_id"};

    public static final String id = "id";

    public static final String route_id = "route_id";

    public static final String status = "status";

    public static final String status_pending = "pending";

    public static final String status_synced = "synced";

    public static final String first_name = "first_name";

    public static final String last_name = "last_name";

    public static final String phone_number = "phone_number";

    public static final String transporter_id = "transporter_id";

    public static final String farmer_id = "farmer_id";

    public static final String jug_id = "jug_id";

    public static final String date = "date";

    public static final String time = "time";

    public static final String modified_date = "modified_date";

    public static final String modified_time = "modified_time";

    public static final String milk_weight = "milk_weight";

    public static final String alcohol = "alcohol";

    public static final String smell = "smell";

    public static final String comments = "comments";

    public static final String tr_transporter_cooling_id = "tr_transporter_cooling_id";

    public static final String tr_farmer_transporter_id = "tr_farmer_transporter_id";

    public static final String density = "density";

    public static final String size = "size";

    public static final String type = "type";

    public static final String currentVolume = "currentVolume";

    public static final String route = "route";
}
