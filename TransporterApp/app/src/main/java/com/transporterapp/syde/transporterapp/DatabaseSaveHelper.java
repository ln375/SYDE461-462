package com.transporterapp.syde.transporterapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danni_000 on 2017-11-23.
 */

    /**
     * Created by ProgrammingKnowledge on 4/3/2015.
     */
    public class DatabaseSaveHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "demoData.db";
        public static final String TABLE_NAME = "tr_farmer_transporter";
        public static final String COL_1 = "ID";
        public static final String COL_2 = "NAME";
        public static final String COL_3 = "SURNAME";
        public static final String COL_4 = "MARKS";
        public static final String COL_7 = "MILK_WEIGHT";
        public static final String COL_9 = "SMELL";
        public static final String COL_11 = "DENSITY";


        public DatabaseSaveHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }

        public boolean insertData(String milkVolume, int smellTest, int densityTest) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_7, milkVolume);
            contentValues.put(COL_9, smellTest);
            contentValues.put(COL_11, densityTest);

                long result = db.insert(TABLE_NAME, null, contentValues);


            if(result == -1)
                return false;
            else
                return true;
        }

        public Cursor getAllData() {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
            return res;
        }
    }


