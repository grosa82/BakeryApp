package com.cs246.bakery.myapplication.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;

/**
 * User class
 */
public class User {

    private Helper helper;
    private Activity activity;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    private GoogleCloudMessaging gcm;
    private final String TAG = "UserClass";

    /**
     * Default constructor
     */
    public User() {}

    /**
     * User constructor for activities calls
     * @param activity Activity context
     */
    public User(Activity activity) {
        this.activity = activity;
        helper = new Helper(this.activity);
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
     * Parse json string to user object
     * @param text json text
     * @return user object
     */
    public User parseJson(String text) {
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

        String jsonString = helper.callWebService(requestPackage).toString();
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            User user = parseJson(jsonString);
            helper.savePreferences("id", Integer.toString(user.id));
            helper.savePreferences("name", user.name);
            helper.savePreferences("phone", user.phone);
            helper.savePreferences("email", user.email);
            helper.savePreferences("token", user.token);
            return user;
        }
    }

    /**
     * Updates user account
     * @param user User object
     * @return Response object
     */
    public Response updateAccount(User user) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("UpdateAccount");
        requestPackage.setParam("id", helper.getPreferences("id"));
        requestPackage.setParam("token", helper.getPreferences("token"));
        requestPackage.setParam("name",user.name);
        requestPackage.setParam("phone",user.phone);
        requestPackage.setParam("email",user.email);
        requestPackage.setParam("password",user.password);
        requestPackage.setParam("regID", user.regID);
        return helper.callWebService(requestPackage).toResponse();
    }

    /**
     * Creates a new account
     * @param user User object
     * @return Response object
     */
    public Response createAccount(User user) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("CreateAccount");
        requestPackage.setParam("name",user.name);
        requestPackage.setParam("phone",user.phone);
        requestPackage.setParam("email",user.email);
        requestPackage.setParam("password",user.password);
        requestPackage.setParam("regID", user.regID);
        return helper.callWebService(requestPackage).toResponse();
    }

    /**
     * Checks if the user is authenticated
     * @return True / False
     */
    public boolean isAuthenticated() {
        String id = helper.getPreferences("id");
        String token = helper.getPreferences("token");
        return (!id.isEmpty() && !token.isEmpty());
    }

    /**
     * Register the device Id
     */
    public void registerDeviceId() {
        gcm = GoogleCloudMessaging.getInstance(activity);
        regID = getRegistrationId();

        if (TextUtils.isEmpty(regID)) {
            registerInBackground();
            Log.d(TAG, "registerGCM - successfully registered with GCM server - regID: " + regID);
        } else {
            Log.d(TAG, "RegID already available. RegId: " + regID);
        }
    }

    private String getRegistrationId() {
        final SharedPreferences prefs = activity.getSharedPreferences(User.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(activity);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("UserForm", "Unexpected Error");
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(activity);
                    }
                    regID = gcm.register("844815750607");
                    msg = "Device registered: " + regID;
                    storeRegistrationId(regID);
                } catch (IOException ex) {
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG, "Registered." + msg);
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = activity.getSharedPreferences(User.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
}
