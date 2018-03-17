package com.transporterapp.syde.transporterapp.ExportData;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.opencsv.CSVWriter;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static android.content.ContentValues.TAG;


/**
 * Created by chari on 2018-01-29.
 */

public class USBTransfer {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static String exportTable(String tableName, Context context) throws IOException {
        String folder = "trFarmerTransporter";
        File f = new File(Environment.getExternalStorageDirectory(), folder);
        if (!f.exists()) {
            f.mkdirs();
        }
        verifyStoragePermissions(Main.instance);

        final File exportFile = new File(f.getPath(), tableName + ".csv");

        try {
            FileWriter testing = new FileWriter(exportFile);
            CSVWriter writer = new CSVWriter(testing, ',');
            final Cursor cursor = dbUtil.selectStatement(tableName, "", "", "", context);
            // Write headers
            final String[] columnNames = cursor.getColumnNames();
            Log.d(TAG, "column names: " + Arrays.asList(columnNames));
            writer.writeNext(columnNames);
            // Write rows
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String[] milkRecord = new String[14];
                milkRecord[0] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.id));
                milkRecord[1] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.transporter_id));
                milkRecord[2] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.farmer_id));
                milkRecord[3] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.jug_id));
                milkRecord[4] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.date));
                milkRecord[5] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.time));
                milkRecord[6] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.milk_weight));
                milkRecord[7] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.alcohol));
                milkRecord[8] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.smell));
                milkRecord[9] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.comments));
                milkRecord[10] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.density));
                milkRecord[11] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
                milkRecord[12] = cursor.getString(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.route_id));
                milkRecord[13] = DatabaseConstants.status_synced;

                writer.writeNext(milkRecord);
                cursor.moveToNext();
            }
            writer.close();
            testing.close();
            return exportFile.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
