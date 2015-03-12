package com.cs246.bakery.myapplication.model;

//import org.ksoap2.serialization.KvmSerializable;
//import org.ksoap2.serialization.PropertyInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * User class
 */
public class User {
    /**
     * Unique key
     */
    public int id;
    /**
     * User full name
     */
    public String name;
    /**
     * User phone number
     */
    public String phone;
    /**
     * Password
     */
    public String password;
    /**
     * Email address
     */
    public String email;
    /**
     * Token
     */
    public String token;
    /**
     * Registration ID
     */
    public String regID;

    public boolean authenticateUser(String username, String password) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("username", "password");

        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost request = new HttpPost("http://cakeapp.ubrainy.com/Help/Api/POST-api-User-Authenticate_email_password");
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
        } catch (Exception ex) {
            httpClient.getConnectionManager().shutdown();
        }

        return true;
    }

    public boolean addUser(String name, String email, String password, String phone) {
        // call web service to add user
        return true;
    }

    public boolean removeUser(String name, String email, String password, String phone) {
        // call web service to remove user
        return true;
    }

}
