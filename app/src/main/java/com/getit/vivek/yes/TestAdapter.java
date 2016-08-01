package com.getit.vivek.yes;

/**
 * Created by kashivivek on 13-06-2016.
 */import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.getit.vivek.db.DatabaseCopier;
import com.getit.vivek.pojo.LatLngBean;

public class TestAdapter
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb ;
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


    public  ArrayList<LatLngBean> getCordinates(String category){
        ArrayList<LatLngBean> arrayList=new ArrayList<>();
        System.out.println("Start get Cord");
        try{
        String[] parts = category.split(":");//houshold:electrician
        String type = parts[1]; // electrician
        String table_name=parts[0]+"_products_table";//household_products_tab
        mDb = mDbHelper.getReadableDatabase();
        String query = "SELECT  * FROM " + table_name+ " where type = "+"'"+type+"'";
        String query2 = "select * from household_products_table";
        Cursor cursor2 = mDb.rawQuery(query, null);
        System.out.println("@@cursor is not null");
        String lattitude = null;
        String longitude = null;
        String id = null;
        String name = null;
        String phone = null;
        String ptype = null;
        String imagepath = null;
        if (cursor2 != null) {
            System.out.println("@@cursor2 is not null");
            if (cursor2.moveToFirst()) {

                do {
                    LatLngBean bean=new LatLngBean();
                    id = cursor2.getString(0);
                    name = cursor2.getString(1);
                    phone = cursor2.getString(2);
                    longitude = cursor2.getString(3);
                    ptype = cursor2.getString(4);
                    imagepath = cursor2.getString(5);
                    lattitude = cursor2.getString(6);
                    System.out.println("@@longitude" + longitude);
/*
                    System.out.println("@@longitude" + longitude);
                    System.out.println("@@lattitude" + lattitude);
                    String temp=longitude+","+lattitude+","+id;
                    al.add(temp);
*/
                    bean.setId(id);
                    bean.setName(name);
                    bean.setPhone(phone);
                    bean.setLongitude(longitude);
                    bean.setType(ptype);
                    bean.setImagepath(imagepath);
                    bean.setLatitude(lattitude);
                    System.out.println(bean.getLatitude());

                    arrayList.add(bean);

                } while (cursor2.moveToNext());
            }

        }
            cursor2.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return arrayList;
        }

    }


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

        if (cursor != null) {
            cursor.close();
        }
        return result;
   }
}