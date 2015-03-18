package com.cs246.bakery.myapplication.model;

import android.content.Context;

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

    private Helper helper = new Helper();
    private Context context;

    /**
     * Default constructor
     */
    public User() {}

    /**
     * User constructor for activities calls
     * @param context Activity context
     */
    public User(Context context) {
        this.context = context;
    }

    /** Unique key */
    public int id;
    /** User full name */
    public String name;
    /** User phone number */
    public String phone;
    /** Password */
    public String password;
    /** Email address */
    public String email;
    /** Token */
    public String token;
    /** Registration ID */
    public String regID;

    /**
     * Authenticates user
     * @param email Email
     * @param password Password
     * @return Null if user not found, otherwise returns the User object
     */
    public User authenticate(String email, String password) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("Authenticate");
        requestPackage.setParam("email", email);
        requestPackage.setParam("password", password);

        String jsonString = helper.getData(requestPackage);
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            User user = helper.parseUser(jsonString);
            helper.savePreferences("id", Integer.toString(user.id), context);
            helper.savePreferences("name", user.name, context);
            helper.savePreferences("phone", user.phone, context);
            helper.savePreferences("email", user.email, context);
            helper.savePreferences("token", user.token, context);
            return user;
        }
    }

    /**
     * Creates a new account
     * @param user User object
     * @return Response object
     */
    public Response createAccount(User user) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("CreateUser");
        requestPackage.setParam("name",user.name);
        requestPackage.setParam("phone",user.phone);
        requestPackage.setParam("email",user.email);
        requestPackage.setParam("password",user.password);
        requestPackage.setParam("regID", user.regID);
        return helper.parseResponse((helper.getData(requestPackage)));
    }

    /**
     * Checks if the user is authenticated
     * @return True / False
     */
    public boolean isAuthenticated() {
        String id = helper.getPreferences("id", context);
        String token = helper.getPreferences("token", context);
        return (!id.isEmpty() && !token.isEmpty());
    }
}
