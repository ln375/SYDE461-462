package com.transporterapp.syde.transporterapp;

import android.database.Cursor;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
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
    public static List<FarmerItem.farmer> convertCursorToFarmerItemList(Cursor farmerList){
        List<FarmerItem.farmer> farmers = new ArrayList<>();

        if (farmerList.getCount()== 0) {
            return null;
        }

        while(farmerList.moveToNext()) {
            String id = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf("id"));
            String first_name = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.first_name));
            String last_name = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.last_name));
            String phone_number = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.phone_number));
            String route_id = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.route_id));

            FarmerItem.farmer temp = FarmerItem.createFarmerItem(id, first_name, last_name, phone_number, route_id);
            farmers.add(temp);
        }

        return farmers;
    }

}
