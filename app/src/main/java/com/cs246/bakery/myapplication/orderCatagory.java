package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class orderCatagory extends ActionBarActivity {
    @Override
    public void onStart() {
        super.onStart();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.text)).setTypeface(tf, Typeface.NORMAL);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_catagory);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_catagory, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void pickTemplate(View view) {
        Intent homepage = new Intent(orderCatagory.this, orderTemplates.class);
        startActivity(homepage);
    }

    public void uploadPic(View view) {
        Intent homepage = new Intent(orderCatagory.this, uploadPicture.class);
        startActivity(homepage);
    }

    public void customCake(View view) {
        Intent homepage = new Intent(orderCatagory.this, customizeCake.class);
        startActivity(homepage);
    }
}
