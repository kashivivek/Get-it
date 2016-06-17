package com.example.vivek.yes;

/**
 * Created by kashivivek on 13-06-2016.
 */import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vivek.db.DatabaseCopier;

public class TestAdapter
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseCopier mDbHelper;

    public TestAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DatabaseCopier(mContext);
    }

    public TestAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

   /* public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM myTable";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }*/
   public boolean validateUser(String pref_username, String pref_password, String table_NAME_user) {

       // select book query
       boolean result =false;
       System.out.println("result intiation" + result);
       String query = "SELECT  * FROM " + table_NAME_user + " where username = '" + pref_username + "'";
       System.out.println("entered cursor-testadapter"+pref_username
       );
       Cursor cursor = mDb.rawQuery(query, null);


       // parse all results
       String username = null;
       String password = null;
       if (cursor != null) {

           //System.out.println("entered cursor-testadapter"+cursor.getString(0));

           if (cursor.moveToFirst()) {

               System.out.println("entered movetofirst");
               do {
                   username = cursor.getString(0);
                   System.out.println("@@@@@@@@@@user:" + cursor.getString(0));
                    System.out.println("@@@@@@@@@@pref_username:" + pref_username);
                   password = cursor.getString(2);
                    System.out.println("@@@@@@@@@@password:" + cursor.getString(1));
                    System.out.println("@@@@@@@@@@pref_password:" + pref_password);
               } while (cursor.moveToNext());
           }
           try{result = username.equalsIgnoreCase(pref_username) && password.equalsIgnoreCase(pref_password);
               System.out.println("result is "+result);}
           catch(NullPointerException e){
               result = false;

           }
       } else
           result = false;
       close();
       return result;
   }
}