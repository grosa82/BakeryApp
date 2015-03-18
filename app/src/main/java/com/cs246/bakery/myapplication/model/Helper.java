package com.cs246.bakery.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

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

    /**
     * Display a temporary message on screen
     * @param message message to display
     */
    public void showAlert(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50); // position
        toast.show();
    }

    /** Constant with the name of the shared preferences */
    public static final String PREFS_NAME = "MyPreferences";

    /**
     * Save information on SharedPreferences
     * @param key
     * @param value
     */
    public void savePreferences(String key, String value, Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Delete shared preferences
     * @param context
     */
    public void deletePreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        preferences.edit().clear().commit();
    }

    /**
     * Get information from SharedPreferences
     * @param key
     * @return value
     */
    public String getPreferences(String key, Context context) {
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
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return response;
    }

    /**
     * Parse json string to user object
     * @param text json text
     * @return user object
     */
    public User parseUser(String text) {
        User user = new User();
        if (text.isEmpty()) {
            user = null;
        } else {
            try {
                JSONObject respObj = new JSONObject(text);
                user.id = respObj.getInt("id");
                user.name = respObj.getString("name");
                user.email = respObj.getString("email");
                user.phone = respObj.getString("phone");
                user.token = respObj.getString("token");
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return user;
    }

    /**
     * Parse a json string to an order object
     * @param text
     * @return Order
     */
    public Order parseOrder(String text) {
        Order order = new Order();
        if (text.isEmpty()) {
            order = null;
        } else {
            try {
                JSONObject respObj = new JSONObject(text);
                order.id = respObj.getInt("id");
                order.nickName = respObj.getString("nickname");
                order.orderDate = parseDate(respObj.getString("orderDate"));
                order.submitted = respObj.getBoolean("submitted");
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return order;
    }

    /**
     * Parse a json date to a Date object
     * @param text
     * @return Date
     */
    private Date parseDate(String text) {
        /* i.e. 2015-02-28T00:29:34 */
        String[] dateTxt = text.split("T");
        String year = dateTxt[0].substring(0, 3);
        return new Date();
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
