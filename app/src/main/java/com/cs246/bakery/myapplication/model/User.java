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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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
