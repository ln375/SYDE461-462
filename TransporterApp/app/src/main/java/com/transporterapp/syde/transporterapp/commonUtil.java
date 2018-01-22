package com.transporterapp.syde.transporterapp;

import android.database.Cursor;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chari on 2018-01-20.
 */

public class commonUtil {

    public final static String FARMER_LIST_TAG_FRAGMENT = "FARMER_LIST_TAG_FRAGMENT";
    public final static String MILK_ENTRY_TAG_FRAGMENT = "MILK_ENTRY_TAG_FRAGMENT";

    /**
     * Converts a cursor representing a farmer list to List<String>
     *
     * @param farmerList
     * @return
     */
    public static List<FarmerItem> convertCursorToFarmerItemList(Cursor farmerList){
        List<FarmerItem> farmers = new ArrayList<>();

        if (farmerList.getCount()== 0) {
            return null;
        }

        while(farmerList.moveToNext()) {
            String id = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf("id"));
            String first_name = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.first_name));
            String last_name = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.last_name));
            String phone_number = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.phone_number));
            String route_id = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.route_id));

            FarmerItem temp = FarmerItem.createFarmerItem(id, first_name, last_name, phone_number, route_id);
            farmers.add(temp);
        }

        return farmers;
    }

    public static List<MilkRecord> convertCursorToMilkRecord(Cursor milkEntryList){
        List<MilkRecord> milkRecords = new ArrayList<>();

        if (milkEntryList.getCount() == 0) {
            return null;
        }

        while(milkEntryList.moveToNext()) {
            String id = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf("id"));
            String transporterId = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.transporter_id));
            String farmerId = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.farmer_id));
            String jugId = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.jug_id));
            String date = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.date));
            String time = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.time));
            String milkWeight = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.milk_weight));
            String alcohol = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.alcohol));
            String smell = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.smell));
            String comments = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.comments));
            String density = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.density));
            String trTransporterCoolingId = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_coolingp_id));

            MilkRecord temp = MilkRecord.createMilkRecord(id, transporterId, farmerId, jugId, date, time, milkWeight, alcohol, smell, comments, density, trTransporterCoolingId);
            milkRecords.add(temp);
        }

        return milkRecords;
    }







}
