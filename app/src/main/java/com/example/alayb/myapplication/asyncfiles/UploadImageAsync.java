package com.example.alayb.myapplication.asyncfiles;


import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class UploadImageAsync extends AsyncTask<Void, Void, String> {

    private String url;
    private OnAsyncResult onAsyncResult;
    private Boolean resultFlag;
    private String charset = "UTF-8";


    private HashMap<String, String> textParams;
    private HashMap<String, File> fileParams;


    public interface OnAsyncResult {
        void onSuccess(String result);

        void onFailure(String result);
    }

    public UploadImageAsync(String url, HashMap<String, String> textParams, HashMap<String, File> fileParams, OnAsyncResult listener) {
        this.url = url;
        this.onAsyncResult = listener;
        resultFlag = false;
        this.textParams = textParams;
        this.fileParams = fileParams;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            MultipartUtility multipart = new MultipartUtility(String.valueOf(url), charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            for (Map.Entry<String, String> entry : textParams.entrySet()) {
                Log.e("Task", "doInBackground: " + entry.getValue());
                multipart.addFormField(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                multipart.addFilePart(entry.getKey(), entry.getValue());
            }

            String response = multipart.finish();
            Log.e("AsyncTask", "doInBackground: " + response);
            resultFlag = true;
            return response;
        } catch (SocketTimeoutException e1) {
            resultFlag = false;
            return "Timeout";

        } catch (Exception e) {
            e.printStackTrace();
            resultFlag = false;
            return "Unexpected Error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (resultFlag) {
            if (onAsyncResult != null) {
                onAsyncResult.onSuccess(result);
            }
        } else {
            if (onAsyncResult != null) {
                onAsyncResult.onFailure(result);
            }
        }
    }
}