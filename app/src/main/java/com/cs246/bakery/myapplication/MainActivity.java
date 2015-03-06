package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
