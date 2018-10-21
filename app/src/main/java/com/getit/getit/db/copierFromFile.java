package com.getit.getit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.getit.getit.yes.DBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kashivivek on 11/1/2017.
 */

public class copierFromFile {
    private SQLiteDatabase db;

    public int insertFromFile(Context context, int resourceId) throws IOException {
        // Reseting Counter
       // db = this.getWritableDatabase();
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
            result++;
        }
        insertReader.close();

        // returning number of inserted rows
        return result;
    }
}
