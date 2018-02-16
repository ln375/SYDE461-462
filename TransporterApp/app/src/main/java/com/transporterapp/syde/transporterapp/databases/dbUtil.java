package com.transporterapp.syde.transporterapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chari on 2018-01-15.
 */


public class dbUtil {
    private SQLiteOpenHelper openHelper;
    public static SQLiteDatabase database;
    public static dbUtil instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private dbUtil(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Return a single instance of dbUtil.
     *
     * @param context the Context
     * @return the instance of dbUtil
     */
    public static dbUtil getInstance(Context context) {
        if (instance == null) {
            instance = new dbUtil(context);
        }
        return instance;
    }

    /**
     *
     * @param context
     * Sets the instance to allow access to the DB
     */
    public static void setInstance(Context context){
        getInstance(context);
        instance.open();
    }

    /**
     * Returns all columns for a selected table (or a specific set of rows in a table)
     * Example Usage: Cursor res = dbUtil.selectStatement(trFarmerTransporter, "", "", context);
     *
     * @param tableName
     * @param whereCondition
     * @param whereOperator
     * @param whereValue
     * @param context
     * @return
     */
    public static Cursor selectStatement(String tableName, String whereCondition, String whereOperator, String whereValue, Context context){
        String sql = "SELECT * FROM " + tableName;
        if (!whereCondition.isEmpty()) {
            sql = sql + " WHERE " + whereCondition + " " + whereOperator + " \"" + whereValue + "\"";
        }

        setInstance(context);

        Cursor cursor = database.rawQuery(sql, null);

        return cursor;
    }

    /**
     * Returns a specific column of a table given particular where conditions
     * Example Usage: List<String> jug_list = dbUtil.selectStatement("jug","id", "transporter_id", "=", "3", context);
     *
     * @param tableName
     * @param columnName
     * @param whereCondition
     * @param whereOperator
     * @param whereValue
     * @param context
     * @return
     */
    public static List<String> selectStatement(String tableName, String columnName, String whereCondition, String whereOperator, String whereValue, Context context){
        List<String> list = new ArrayList<>();

        String sql = "SELECT " + columnName + " FROM " + tableName;
        if (!whereCondition.isEmpty()) {
            sql = sql + " WHERE " + whereCondition + " " + whereOperator + " " + whereValue;
        }

        setInstance(context);

        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    /**
     * Returns a specific column given multiple where conditions
     * Example Usage: List<String> jug_list = dbUtil.selectStatement("jug","id", Arrays.asList("type", "transporter_id"), Arrays.asList("=", ">="), Arrays.asList("Aluminum", "3"));
     *
     * @param tableName
     * @param columnName
     * @param whereCondition
     * @param whereOperators
     * @param whereValue
     * @param context
     * @return
     */
    public static List<String> selectStatement(String tableName, String columnName, List<String> whereCondition, List<String> whereOperators, List<String> whereValue, Context context){
        List<String> list = new ArrayList<>();

        String sql = "SELECT " + columnName + " FROM " + tableName;
        if (!whereCondition.isEmpty()) {

            String whereStatement = convertWhereConditionsToString(whereCondition, whereOperators, whereValue);
            sql = sql + whereStatement;
        }

        setInstance(context);

        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }



    /**
     * Inserts a row into a table
     * Example Usage: dbUtil.insertStatement(trFarmerTransporter, milkEntryColumns, values, this);
     *
     * @param tableName
     * @param columnNames
     * @param values
     * @param context
     * @return
     */
    public static boolean insertStatement(String tableName, List<String> columnNames, List<String> values, Context context){
        setInstance(context);

        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < columnNames.size(); i++) {
            contentValues.put(columnNames.get(i), values.get(i));
        }


        long result = database.insert(tableName, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;

    }

    public static void updateStatement(String tableName, String col, String value,  String whereCondition, String whereOperator, String whereValue, Context context){
        setInstance(context);

        String sql = "UPDATE " + tableName + " SET " + col + " = " + value;
        if (!whereCondition.isEmpty()) {
            sql = sql + " WHERE " + whereCondition + " " + whereOperator + " " + whereValue;
        }


        database.rawQuery(sql, null);
    }

    /**
     * Deletes a row in a table based on a specific condition
     * Example Usage: dbUtil.deleteStatement(trFarmerTransporter, "id", ">", "10", this);
     *
     * @param tableName
     * @param whereCondition
     * @param whereOperator
     * @param whereValue
     * @param context
     * @return
     */
    public static boolean deleteStatement(String tableName, String whereCondition, String whereOperator, String whereValue, Context context) {
        setInstance(context);
        return database.delete(tableName, whereCondition + whereOperator + whereValue, null) > 0;
    }

    /**
     * Deletes a row in a table based on multiple conditions
     * Example Usage: dbUtil.deleteStatement(trFarmerTransporter, Arrays.asList("id"), Arrays.asList(">"), Arrays.asList("10"), this);
     *
     * @param tableName
     * @param whereConditions
     * @param whereOperators
     * @param whereValues
     * @param context
     * @return
     */
    public static boolean deleteStatement(String tableName, List<String> whereConditions, List<String> whereOperators, List<String> whereValues, Context context) {
        setInstance(context);
        String whereCond = "";
        for (int i = 0; i < whereConditions.size(); i++) {
            whereCond = whereCond + whereConditions.get(i) + whereOperators.get(i) + "? AND";
        }
        whereCond = whereCond.substring(0, whereCond.length() - 4);
        return database.delete(tableName, whereCond, (String[]) whereValues.toArray()) > 0;
    }

    /**
     * Converts the where conditions list to a string
     * Example Usage: String whereStatement = convertWhereConditionsToString(Arrays.asList("id", "type"), Arrays.asList(">", "="), Arrays.asList("3", "Plastic");
     *
     * @param whereCondition
     * @param whereOperators
     * @param whereValue
     * @return
     */
    private static String convertWhereConditionsToString(List<String> whereCondition, List<String> whereOperators, List<String> whereValue) {
        String result = "";
        int index = 0;
        if (whereCondition.size() == whereValue.size()) {
            String add = " WHERE {} {} '{}' AND";
            for (String item : whereCondition) {
                result = result + add;
                result.replaceFirst("\\{\\}", item);
                result.replaceFirst("\\{\\}", whereOperators.get(index));
                result.replaceFirst("\\{\\}", whereValue.get(index));
                index++;
            }

            result = result.substring(0, result.length() - 4);
        }
        return result;
    }
}
