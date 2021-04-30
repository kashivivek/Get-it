package com.getit.getit.yes;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.annotation.NonNull;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by kashivivek on 10/26/2017.
 */

public class FileDownloader extends AsyncTask<String, String, String> {
    private String resp;
    ProgressDialog progressDialog;
    private Context mContext;

    public FileDownloader(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        Uri image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/stellar-cumulus-108603.appspot.com/o/Get_it.sql?alt=media&token=a181a0f7-6a1e-4e36-bd98-4965b4278d3a");
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(image_uri);
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,"Get_it.sql");
        System.out.println("jaffffffffaaaaa: "+Environment.DIRECTORY_DOWNLOADS+"/Get_it.sql");
        downloadManager.enqueue(request);
        return resp;
    }


    @Override
    protected void onPostExecute(String result) {
        System.out.println("DB Downloaded");
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {

    }
}
