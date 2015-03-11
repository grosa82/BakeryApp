package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends ActionBarActivity {

    Helper helper = new Helper();

    /*
    // code for checking for Google play services
     public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


     // Substitute you own sender ID here. This is the project number you got
     // from the API Console, as described in "Getting Started."

    String SENDER_ID = "Your-Sender-ID";


     // Tag used on log messages
    static final String TAG = "GCMDemo";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_main);
/*
         context = getApplicationContext();

        // check for updates with Google play services for GCM
        // Check device for Play Services APK.
         if (checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }*/
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                // show error message
                finish();
            }
            return false;
        }
        return true;
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.textView)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) findViewById(R.id.createAccount)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) findViewById(R.id.signIn)).setTypeface(tf, Typeface.NORMAL);

        // If user is authenticated, redirects to the summary page
        String id = helper.getPreferences("id", MainActivity.this.getApplicationContext());
        String token = helper.getPreferences("token", MainActivity.this.getApplicationContext());

        if (!id.isEmpty() && !token.isEmpty())
            startActivity(new Intent(MainActivity.this, account_summary.class));

        String android_id = Settings.Secure.getString(MainActivity.this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_login:
                startActivity(new Intent(MainActivity.this, signOn.class));
                return true;
            case R.id.action_create:
                startActivity(new Intent(MainActivity.this, UserForm.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signIn(View view) {
        Intent homepage = new Intent(MainActivity.this, UserForm.class);
        startActivity(homepage);
    }

    public void login(View view) {
        Intent homepage = new Intent(MainActivity.this, signOn.class);
        startActivity(homepage);
    }
}
