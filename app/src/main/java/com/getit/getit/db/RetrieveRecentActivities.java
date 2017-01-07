package com.getit.getit.db;

/**
 * Created by vIvEk on 05-01-2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.getit.getit.pojo.Records;

import java.util.LinkedList;
import java.util.List;

public class RetrieveRecentActivities extends SQLiteOpenHelper {

    // database version
    private static final int database_VERSION = 1;
    // database name
    private static final String database_NAME = "Get_it_db";
    private static final String table_NAME = "recent_activity";
    private static final String service_date = "service_date";
    private static final String service_name = "service_name";
    private static final String service_category = "service_category";

    private static final String[] COLUMNS = {service_date, service_name, service_category};

    public RetrieveRecentActivities(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_TABLE = "CREATE TABLE " + table_NAME + " ( " + "service_date TEXT, " + "service_name TEXT, " + "service_category TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop books table if already exists
        db.execSQL("DROP TABLE IF EXISTS " + table_NAME);
        this.onCreate(db);
    }

    public void createRecords(Records records) {
        // get reference of the get_it_db2 database
        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(service_date, records.getServiceDate());
        values.put(service_name, records.getName());
        values.put(service_category, records.getCategory());

        // insert data
        db.insert(table_NAME, null, values);

        // close database transaction
        db.close();
    }

    public List getAllRecords() {
        List records_list = new LinkedList();

        // select records query
        String query = "SELECT  * FROM " + table_NAME;

        // get reference of the Get_it_DB database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        Records records = null;
        if (cursor.moveToFirst()) {
            do {
                records = new Records();
                records.setServiceDate(cursor.getString(0));
                records.setName(cursor.getString(1));
                records.setCategory(cursor.getString(2));

                // Add book to books
                records_list.add(records);
            } while (cursor.moveToNext());
        }
        return records_list;
    }
}