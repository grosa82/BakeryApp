package com.cs246.bakery.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;

/**
 * User class
 */
public class User {

    private Helper helper = new Helper();
    private Context context;
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

    /**
     * Register the device Id
     */
    public void registerDeviceId() {
        gcm = GoogleCloudMessaging.getInstance(context);
        regID = getRegistrationId();

        if (TextUtils.isEmpty(regID)) {
            registerInBackground();
            Log.d(TAG, "registerGCM - successfully registered with GCM server - regID: " + regID);
        } else {
            Log.d(TAG, "RegID already available. RegId: " + regID);
        }
    }

    private String getRegistrationId() {
        final SharedPreferences prefs = context.getSharedPreferences(User.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
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
                        gcm = GoogleCloudMessaging.getInstance(context);
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
        final SharedPreferences prefs = context.getSharedPreferences(User.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
}
