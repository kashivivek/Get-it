package com.getit.getit.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseCopier extends SQLiteOpenHelper
{
    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME = "Get_it.db";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DatabaseCopier(Context context)
    {
        super(context, DB_NAME, null, 1);// 1? Its database Version
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() throws IOException
    {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();

                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");

        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    public void copyDataFromFile() throws IOException {
        DatabaseCopier databaseCopier = new DatabaseCopier(mContext);
        this.mDataBase = databaseCopier.getWritableDatabase();
        //InputStream insertsStream = new FileInputStream("/storage/emulated/0/Android/data/com.getit.getit.yes/files/Get_it.sql");
        File file = new File("/storage/emulated/0/Android/data/com.getit.getit.yes/files/Get_it.sql");
        System.out.println("@@@File absolute path"+file.getAbsolutePath());
        System.out.println("@@@File can be read"+file.canRead());
        //insertsStream.close();
       // BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        /*try {
            while (insertReader.ready()) {
                String insertStmt = insertReader.readLine();
                mDataBase.execSQL(insertStmt);
            }
            System.out.println("Data copied @@@@@@@@");
            insertReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    //Copy the database from assets
    private void copyDataBase() {

        try {
            InputStream mInput = mContext.getAssets().open(DB_NAME);
            //FileInputStream mInput = null;
            //mInput = new FileInputStream(mContext.getFilesDir()+"/Download/Get_it.db");

        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}