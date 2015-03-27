package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;


public class OrderedCake extends ActionBarActivity {
    private Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_cake);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();

        TextView textView = (TextView) findViewById(R.id.textView16);
        textView.setText("");

        TextView textView2 = (TextView) findViewById(R.id.textView12);
        textView2.setText("");
    }
}
