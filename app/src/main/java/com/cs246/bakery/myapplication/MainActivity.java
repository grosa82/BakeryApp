package com.cs246.bakery.myapplication;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.CompanyInfo;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.User;

public class MainActivity extends Activity {
    private Helper helper = new Helper(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class LoadCompanyInfo extends AsyncTask<Void, Void, CompanyInfo> {

        @Override
        protected CompanyInfo doInBackground(Void... params) {
            CompanyInfo companyInfo = new CompanyInfo();
            return companyInfo.getCompanyInfo();
        }

        @Override
        public void onPostExecute(CompanyInfo info) {
            ((TextView)findViewById(R.id.companyName)).setText(info.name);
            ((TextView)findViewById(R.id.phone)).setText(info.phone);

            helper.savePreferences("companyName", info.name);
            helper.savePreferences("companyAddress", info.address);
            helper.savePreferences("companyEmail", info.email);
            helper.savePreferences("companyPhone", info.phone);
            helper.savePreferences("companyWebsite", info.website);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_main);

        new LoadCompanyInfo().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.companyName)).setTypeface(tf, Typeface.NORMAL);

        // If user is authenticated, redirects to the summary page
        User user = new User(MainActivity.this);
        user.registerDeviceId();
        if (user.isAuthenticated())
            startActivity(new Intent(MainActivity.this, MyCakes.class));

        String android_id = Settings.Secure.getString(MainActivity.this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void register(View view) {
        Intent homepage = new Intent(MainActivity.this, CreateAccount.class);
        startActivity(homepage);
    }

    public void login(View view) {
        Intent homepage = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(homepage);
    }
}
