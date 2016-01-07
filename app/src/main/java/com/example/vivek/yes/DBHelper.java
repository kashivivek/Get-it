package com.example.vivek.yes;

/**
 * Created by vIvEk on 05-01-2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // database version
    private static final String database_NAME = "Get_it_db";
    private static final int database_VERSION = 1;
    private static final String table_BOOKS = "books";
    private static final String table_NAME = "users";
    private static final String book_ID = "id";
    private static final String book_TITLE = "title";
    private static final String book_AUTHOR = "author";
    private static final String[] COLUMNS = {book_ID, book_TITLE, book_AUTHOR};
    private static String pref_username;
    private static String pref_password;

    public DBHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("@@@@@@@@@@iloveanusha" + table_NAME + "@@@@@@@@@@@");
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE  " + table_NAME + "( " + "username TEXT, " + "password TEXT )";
        db.execSQL(CREATE_BOOK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop books table if already exists
        db.execSQL("DROP TABLE IF EXISTS " + table_NAME);
        this.onCreate(db);
    }

    public void generateTable() {
        // get reference of the BookDB database
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("insert into " + table_NAME + " values('vivek','vivek123')");
        db.close();
    }

    public boolean validateUser(String pref_username, String pref_password) {

        // select book query
        boolean result;
        String query = "SELECT  * FROM " + table_NAME + " where username = '" + pref_username + "';";

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
            result = username.equals(pref_username) && password.equals(pref_password);
        } else
            result = false;
        return result;
    }

}