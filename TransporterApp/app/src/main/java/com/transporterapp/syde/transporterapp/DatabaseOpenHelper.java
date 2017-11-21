package com.transporterapp.syde.transporterapp;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.content.Context;


/**
 * Created by dannie on 2017-11-21.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "demoData.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
