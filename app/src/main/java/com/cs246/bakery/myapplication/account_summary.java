package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;


public class account_summary extends ActionBarActivity {

    Helper helper = new Helper();

    @Override
    protected void onResume() {
        super.onResume();
        String name = helper.getPreferences("name", account_summary.this.getApplicationContext());
        if (!name.isEmpty()) {
            TextView textView = ((TextView) findViewById(R.id.title));
            if (textView != null)
                textView.setText("Welcome, " + name);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_signOut:
                helper.deletePreferences(account_summary.this.getApplicationContext());
                startActivity(new Intent(account_summary.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void chooseType(View view) {
        Intent homepage = new Intent(account_summary.this, orderCatagory.class);
        startActivity(homepage);
    }

}
