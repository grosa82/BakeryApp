package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;


public class OrderedCake extends ActionBarActivity {
    private Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_cake);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.orderedCakeTitle)).setTypeface(tf, Typeface.NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String message = intent.getStringExtra(account_summary.CAKE_NICKNAME);
        String message2 = intent.getStringExtra(account_summary.DATE);

        TextView textView = (TextView) findViewById(R.id.textView16);
        textView.setText(message2);

        TextView textView2 = (TextView) findViewById(R.id.textView12);
        textView2.setText(message);
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
}
