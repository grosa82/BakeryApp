package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.User;

public class MainActivity extends ActionBarActivity {
    private Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_main);
    }

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

        // If user is authenticated, redirects to the summary page
        User user = new User(MainActivity.this);
        user.registerDeviceId();
        if (user.isAuthenticated())
            startActivity(new Intent(MainActivity.this, account_summary.class));

        String android_id = Settings.Secure.getString(MainActivity.this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            case R.id.action_create:
                startActivity(new Intent(MainActivity.this, UserForm.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void register(View view) {
        Intent homepage = new Intent(MainActivity.this, UserForm.class);
        startActivity(homepage);
    }

    public void login(View view) {
        Intent homepage = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(homepage);
    }
}
