package com.getit.getit.yes;

/**
 * Created by vIvEk on 05-01-2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {

    // database version
    private static final String database_NAME = "Get_it_db";
    private static final int database_VERSION = 1;
    private static final String table_NAME_user = "users";
    private static final String table_NAME_groceries = "groceries_products_table";
    private static final String table_NAME_household = "household_products_table";
    private static final String table_NAME_food = "food_products_table";
    private static final String table_NAME_recent = "recent_services_table";
    private static final String book_ID = "id";
    private static final String book_TITLE = "title";
    private static final String book_AUTHOR = "author";
    private static final String[] COLUMNS = {book_ID, book_TITLE, book_AUTHOR};
    private static String pref_username;
    private static String pref_password;
    Context ctx;

    public DBHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
        ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*System.out.println("@@@@@@@@@@iloveanusha" + table_NAME + "@@@@@@@@@@@");*/
        // SQL statement to create book table
        String CREATE_USER_TABLE = "CREATE TABLE  " + table_NAME_user + "( " + "username TEXT, " + "password TEXT, " + "question TEXT, " + "answer TEXT, " + " imagepath TEXT, " + " ageplace TEXT, " + " phone INTEGER )";
        String CREATE_HOUSEPRODUCTS_TABLE = "CREATE TABLE  " + table_NAME_household + "( " + "pid TEXT, " + "name TEXT , " + "phone INTEGER, " + "longitude TEXT, " + " latitude TEXT, " + " imagepath TEXT, " + " type TEXT , " + " cost TEXT )";
        String CREATE_FOODPRODUCTS_TABLE = "CREATE TABLE  " + table_NAME_food + "( " + "pid TEXT, " + "name TEXT , " + "phone INTEGER, " + "longitude TEXT, " + " latitude TEXT, " + " imagepath TEXT, " + " type TEXT , " + " cost TEXT )";
        String CREATE_GROCERIESPRODUCTS_TABLE = "CREATE TABLE  " + table_NAME_groceries + "( " + "pid TEXT, " + "name TEXT , " + "phone INTEGER, " + "longitude TEXT, " + " latitude TEXT, " + " imagepath TEXT, " + " type TEXT , " + " cost TEXT )";
        String CREATE_RECENTSERVICES_TABLE = "CREATE TABLE  " + table_NAME_recent + "( " + "pid TEXT, " + "name TEXT , " + "datestamp INTEGER, " + "cost TEXT, " + " type TEXT, " + " mode TEXT)";

        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = ctx.getResources().openRawResource(R.raw.get_it_db2);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        try {
            while (insertReader.ready()) {
                String insertStmt = insertReader.readLine();
                db.execSQL(insertStmt);
                result++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            insertReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*db.execSQL(CREATE_USER_TABLE);*/
       /* db.execSQL(CREATE_HOUSEPRODUCTS_TABLE);
        db.execSQL(CREATE_FOODPRODUCTS_TABLE);
        db.execSQL(CREATE_GROCERIESPRODUCTS_TABLE);
        db.execSQL(CREATE_RECENTSERVICES_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop books table if already exists
        /*db.execSQL("DROP TABLE IF EXISTS " + table_NAME_user);*/
       /* db.execSQL("DROP TABLE IF EXISTS " + table_NAME_household);
        db.execSQL("DROP TABLE IF EXISTS " + table_NAME_food);
        db.execSQL("DROP TABLE IF EXISTS " + table_NAME_groceries);
        db.execSQL("DROP TABLE IF EXISTS " + table_NAME_recent);*/
        System.out.println("entered on upgrade in DBHelper");



        this.onCreate(db);
    }

    public void generateTable() {
        // get reference of the BookDB database
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("$$$$$$$$$$$$$$$");
        db.execSQL("insert into " + table_NAME_user + "(username,password) values('com.avsoftsol.getit.vivek','vivek123')");
        System.out.println("$$$$$$$$$$$$$$$");
        db.close();
    }

    public boolean validateUser(String pref_username, String pref_password) {

        // select book query
        boolean result;
        String query = "SELECT  * FROM " + table_NAME_user + " where username = '" + pref_username + "';";

        // get reference of the BookDB database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        String username = null;
        String password = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    username = cursor.getString(0);
                   /* System.out.println("@@@@@@@@@@user" + cursor.getString(0));
                    System.out.println("@@@@@@@@@@pref_username" + pref_username);*/
                    password = cursor.getString(1);
                    /*System.out.println("@@@@@@@@@@password" + cursor.getString(1));
                    System.out.println("@@@@@@@@@@pref_password" + pref_password);*/
                } while (cursor.moveToNext());
            }
            try{result = username.equals(pref_username) && password.equals(pref_password);}
            catch(NullPointerException e){
                result = false;
            }
        } else
            result = false;
        return result;
    }

}