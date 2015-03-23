package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cs246.bakery.myapplication.model.Helper;


public class profile_summary extends ActionBarActivity {

    private Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_summary);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_about_us) {
            return true;
        }

        if (id == R.id.call_me) {
            return true;
        }

        if (id == R.id.my_profile) {
            return true;
        }

        if (id == R.id.action_signOut) {
            helper.signOut();
            Intent homepage = new Intent(this, MainActivity.class);
            startActivity(homepage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
