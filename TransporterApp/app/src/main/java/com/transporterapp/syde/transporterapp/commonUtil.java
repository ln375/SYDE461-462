package com.transporterapp.syde.transporterapp;

import android.content.Context;
import android.database.Cursor;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.HistMilkRecord;
import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.DataStructures.Routes;
import com.transporterapp.syde.transporterapp.DataStructures.TransporterItem;
import com.transporterapp.syde.transporterapp.History.HistListFrag;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chari on 2018-01-20.
 */

public class commonUtil {

    public final static String LOGIN_TAG_FRAGMENT = "FARMER_LIST_TAG_FRAGMENT";
    public final static String FARMER_LIST_TAG_FRAGMENT = "FARMER_LIST_TAG_FRAGMENT";
    public final static String MILK_ENTRY_TAG_FRAGMENT = "MILK_ENTRY_TAG_FRAGMENT";
    public final static String HIST_LIST_TAG_FRAGMENT = "HIST_LIST_TAG_FRAGMENT";
    public final static String HIST_REC_TAG_FRAGMENT = "HIST_REC_TAG_FRAGMENT";
    public final static String EXPORT_TAG_FRAGMENT = "EXPORT_TAG_FRAGMENT";
    public final static String DEVICE_LIST_FRAG = "DEVICE_LIST_FRAGMEMT";

    /**
     * Converts a cursor representing a farmer list to List<FarmerItem>
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
            String id = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.id));
            String first_name = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.first_name));
            String last_name = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.last_name));
            String phone_number = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.phone_number));
            String route_id = farmerList.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.route_id));

            FarmerItem temp = FarmerItem.createFarmerItem(id, first_name, last_name, phone_number, route_id);
            farmers.add(temp);
        }

        return farmers;
    }

    /**
     * Converts a cursor representing a milk record list to List<MilkRecord>
     *
     * @param milkEntryList
     * @return
     */
    public static List<MilkRecord> convertCursorToMilkRecordList(Cursor milkEntryList){
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
            String trTransporterCoolingId = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
            String routeId = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.route_id));
            String status = milkEntryList.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.status));

            MilkRecord temp = MilkRecord.createMilkRecord(id, transporterId, farmerId, jugId, date, time, milkWeight, alcohol, smell, comments, density, trTransporterCoolingId, routeId, status);
            milkRecords.add(temp);
        }

        return milkRecords;
    }

    public static List<HistMilkRecord> convertCursorToHistMilkRecordList(Cursor milkEntryList){
        List<HistMilkRecord> milkRecords = new ArrayList<>();

        if (milkEntryList.getCount() == 0) {
            return null;
        }

        while(milkEntryList.moveToNext()) {
            String id = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf("id"));
            String transporterId = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.transporter_id));
            String farmerId = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.farmer_id));
            String jugId = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.jug_id));
            String date = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.modified_date));
            String time = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.modified_time));
            String milkWeight = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.milk_weight));
            String alcohol = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.alcohol));
            String smell = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.smell));
            String comments = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.comments));
            String density = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.density));
            String trTransporterCoolingId = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
            String routeId = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.route_id));
            String status = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.status));
            String trFarmerTransporter = milkEntryList.getString(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.tr_farmer_transporter_id));

            HistMilkRecord temp = HistMilkRecord.createMilkRecord(id, transporterId, farmerId, jugId, date, time, milkWeight, alcohol, smell, comments, density, trTransporterCoolingId, routeId, status, trFarmerTransporter);
            milkRecords.add(temp);
        }

        return milkRecords;
    }

    public static MilkRecord convertCursorToMilkRecord(Cursor milkRecord) {
        while(milkRecord.moveToNext()) {
            String id = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf("id"));
            String transporterId = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.transporter_id));
            String farmerId = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.farmer_id));
            String jugId = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.jug_id));
            String date = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.date));
            String time = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.time));
            String milkWeight = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.milk_weight));
            String alcohol = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.alcohol));
            String smell = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.smell));
            String comments = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.comments));
            String density = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.density));
            String trTransporterCoolingId = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
            String routeId = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.route_id));
            String status = milkRecord.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.status));

            return new MilkRecord(id, transporterId, farmerId, jugId, date, time, milkWeight, alcohol, smell, comments, density, trTransporterCoolingId, routeId, status);
        }
        return null;
    }

    /**
     * Converts a cursor representing a farmer item to FarmerItem
     *
     * @param farmer
     * @return
     */
    public static FarmerItem convertCursortToFarmerItem(Cursor farmer) {
        while(farmer.moveToNext()){
            String id = farmer.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.id));
            String firstName = farmer.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.first_name));
            String lastName = farmer.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.last_name));
            String phoneNumber = farmer.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.phone_number));
            String routeId = farmer.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.route_id));

            return new FarmerItem(id, firstName, lastName, phoneNumber, routeId);
        }
        return null;
    }

    /**
     * Converts a cursor representing a transporter to TransporterItem
     *
     * @param transporter
     * @return
     */
    public static TransporterItem convertCursortToTransporterItem(Cursor transporter) {
        while(transporter.moveToNext()){
            String id = transporter.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.id));
            String firstName = transporter.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.first_name));
            String lastName = transporter.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.last_name));
            String phoneNumber = transporter.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.phone_number));
            String routeId = transporter.getString(Arrays.asList(DatabaseConstants.colFarmer).indexOf(DatabaseConstants.route_id));

            return new TransporterItem(id, firstName, lastName, phoneNumber, routeId);
        }
        return null;
    }

    /**
     * Converts a cursor representing a route list to List</Routes>
     *
     * @param routes
     * @return
     */
    public static List<Routes> convertCursortToRouteItem(Cursor routes) {
        java.util.List<Routes> routeList = new ArrayList<>();

        if (routes.getCount()== 0) {
            return null;
        }

        while(routes.moveToNext()) {
            String id = routes.getString(Arrays.asList(DatabaseConstants.colRoute).indexOf(DatabaseConstants.id));
            String routeOrder = routes.getString(Arrays.asList(DatabaseConstants.colRoute).indexOf(DatabaseConstants.route));

            Routes temp = Routes.createRoutes(id, routeOrder);
            routeList.add(temp);
        }

        return routeList;
    }

    /**
     * Converts a cursor representing a jug to Jug
     *
     * @param jug
     * @return
     */
    public static Jug convertCursorToJug(Cursor jug) {
        while(jug.moveToNext()){
            String id = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.id));
            String size = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.size));
            String type = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.type));
            String transporterId = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.transporter_id));
            String currentVolume = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.currentVolume));

            return new Jug(id, size, type, transporterId, currentVolume);
        }
        return null;
    }

    public static String getMostRecentId(Cursor transaction) {
        while (transaction.moveToNext()) {
            return transaction.getString(0);
        }
        return "";
    }

    public static List<Jug> convertCursorToJugList(Cursor jug){
        List<Jug> jugList = new ArrayList<>();

        if (jug.getCount() == 0) {
            return null;
        }

        while(jug.moveToNext()) {
            String id = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.id));
            String size = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.size));
            String type = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.type));
            String transporterId = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.transporter_id));
            String currentVolume = jug.getString(Arrays.asList(DatabaseConstants.colJug).indexOf(DatabaseConstants.currentVolume));

            Jug temp = new Jug(id, size, type, transporterId, currentVolume);
            jugList.add(temp);
        }

        return jugList;
    }


    public static String getQualityRating(String index, Context context) {
        List<String> qualityRating = dbUtil.selectStatement("qualityratings", "rating", "id", "=", index, context);
        if (qualityRating != null && qualityRating.size() > 0) {
            return qualityRating.get(0);
        } else {
            return "";
        }
    }

    public static String getMonthofYear(int index) {
        String temp = "";
        switch (index) {
            case 1:
                temp = "January";
                break;
            case 2:
                temp = "February";
                break;
            case 3:
                temp = "March";
                break;
            case 4:
                temp = "April";
                break;
            case 5:
                temp = "May";
                break;
            case 6:
                temp = "June";
                break;
            case 7:
                temp = "July";
                break;
            case 8:
                temp = "August";
                break;
            case 9:
                temp = "September";
                break;
            case 10:
                temp = "October";
                break;
            case 11:
                temp = "November";
                break;
            case 12:
                temp = "December";
                break;
        }
        return temp;
    }

    public static byte[] convertFileToBytes(String filePath) {
        try{
            File file = new File(filePath);
            //init array with file length
            byte[] bytesArray = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();

            return bytesArray;
        } catch (IOException e) {

        }
        return null;
    }

    public static String getCurrentDate() {
        //Get the current date
        Calendar calander = Calendar.getInstance();
        int cDay = calander.get(Calendar.DAY_OF_MONTH);
        int cMonth = calander.get(Calendar.MONTH) + 1;
        int cYear = calander.get(Calendar.YEAR);

        return getMonthofYear(cMonth) + " " + Integer.toString(cDay) + ", " + Integer.toString(cYear);
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String getMonthString(int i) {
        switch (i) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return null;
        }


    }

    public static List<String> compareHistMilkRecords (HistMilkRecord firstRecord, HistMilkRecord secondRecord) throws ParseException {
        List<String> changes = new ArrayList<>();

        DateFormat time = new SimpleDateFormat("HH:mm");

        if (!firstRecord.getJugId().equalsIgnoreCase(secondRecord.getJugId())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - JugID updated to: " + secondRecord.getJugId());
        }

        if (!firstRecord.getMilkWeight().equalsIgnoreCase(secondRecord.getMilkWeight())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Milk quantity updated to: " + secondRecord.getMilkWeight() + " L");
        }

        if (!firstRecord.getAlcohol().equalsIgnoreCase(secondRecord.getAlcohol())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Alcohol Test updated to: " + secondRecord.getAlcohol());
        }

        if (!firstRecord.getSmell().equalsIgnoreCase(secondRecord.getSmell())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Smell Test updated to: " + secondRecord.getSmell());
        }

        if (!firstRecord.getDensity().equalsIgnoreCase(secondRecord.getDensity())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Density Test updated to: " + secondRecord.getDensity());
        }

        if (!firstRecord.getComments().equalsIgnoreCase(secondRecord.getComments())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Comments updated to: " + secondRecord.getComments());
        }

        if (!firstRecord.getStatus().equalsIgnoreCase(secondRecord.getStatus())) {
            changes.add(firstRecord.getDate() + " " + time.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Status updated to: " + secondRecord.getStatus());
        }
        return changes;
    }
}
