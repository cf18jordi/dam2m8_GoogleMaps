package com.example.projectem8;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ApiThread extends AsyncTask<Void, Void, String> {

    public ApiThread(double lat, double lng) {}


    @Override
    protected String doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


            String data = bufferedReader.readLine();
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void onPostExecute(String data) {

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(data);
            jObject = jObject.getJSONObject("results");

            String sunrise = jObject.getString("sunrise");
            String sunset = jObject.getString("sunset");


            Log.i("logtest", "--Sunrise---->" + sunrise);
            Log.i("logtest", "--Sunset---->" + sunset);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

