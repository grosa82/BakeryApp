package com.cs246.bakery.myapplication.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs246.bakery.myapplication.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Helper class
 */
public class Helper {

    /** Constant with the name of the shared preferences */
    private static final String PREFS_NAME = "MyPreferences";
    /** Context */
    private Context context;

    public Helper(Activity activity) {
        context = activity;
    }

    /**
     * Display a temporary message on screen
     * @param message message to display
     */
    public void showAlert(String message) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_toast,
           (ViewGroup)((Activity)context).findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     * Save information on SharedPreferences
     * @param key
     * @param value
     */
    public void savePreferences(String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Delete shared preferences
     */
    public void deletePreferences() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        preferences.edit().clear().commit();
    }

    /**
     * Get information from SharedPreferences
     * @param key
     * @return value
     */
    public String getPreferences(String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, "").toString();
    }

    /**
     * Parse json string response to an object Response
     * @param text json string
     * @return Response object
     */
    public Response parseResponse(String text) {
        Response response = new Response();
        if (text.isEmpty()) {
            response.success = false;
            response.message = "Returned Null";
        } else {
            try {
                JSONObject respObj = new JSONObject(text);
                response.success = respObj.getBoolean("success");
                response.message = respObj.getString("message");
                response.exception = respObj.getString("exception");
                response.createdId = respObj.getInt("id");
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return response;
    }



    /**
     * Parse a json date to a Date object
     * @param text
     * @return Date
     */
    public Date parseDate(String text) {
        /* i.e. 2015-02-28T00:29:34 */
        String[] dateTxt = text.split("T");
        String year = dateTxt[0].substring(0, 3);
        return new Date();
    }

    /**
     * Parse boolean
     * @param text
     * @return
     */
    public Boolean parseBool(String text) {
        return text.equals("Y");
    }

    /**
     * Make a request to the web service layer
     * @param requestPackage
     * @return string returned from server
     */
    public String getData(RequestPackage requestPackage) {

        Log.i(this.getClass().getName(), requestPackage.getUri());

        BufferedReader reader = null;
        String uri = "http://cakeapp.toughland.com/api/webapi/" + requestPackage.getUri();
        if (requestPackage.getMethod().equals("GET")) {
            uri += "?" + requestPackage.getEncodedParams() + "&json=true";
        }

        try {
            URL url = new URL(uri);
            if (requestPackage.getMethod().equals("POST"))
                url = new URL(uri + "?json=true");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod(requestPackage.getMethod());

            if (requestPackage.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(requestPackage.getEncodedParams());
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + '\n');
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
