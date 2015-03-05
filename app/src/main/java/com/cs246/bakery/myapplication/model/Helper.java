package com.cs246.bakery.myapplication.model;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ricardo on 2/26/2015.
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

    public Boolean checkResponse(String response)
    {
        return response.replace("\n","").equals("true");
    }

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

    public String getData(RequestPackage requestPackage) {
        BufferedReader reader = null;
        String uri = requestPackage.getUri();
        if (requestPackage.getMethod().equals("GET")) {
            uri += "?" + requestPackage.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
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
