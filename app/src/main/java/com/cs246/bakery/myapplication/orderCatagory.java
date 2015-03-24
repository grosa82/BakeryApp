package com.cs246.bakery.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;


public class orderCatagory extends ActionBarActivity {
    private Helper helper = new Helper(this);

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_catagory);
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

        if (id == R.id.action_about_us) {
            helper.displayCompanyInfo().show();
            return true;
        }

        if (id == R.id.call_me) {
            helper.displayOkCancelDialog("We can call you to help you with your order", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper.callMe();
                }
            }).show();
            return true;
        }

        if (id == R.id.my_profile) {
            helper.goToProfile();
            return true;
        }

        if (id == R.id.action_signOut) {
            helper.signOut();
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
        Intent homepage = new Intent(orderCatagory.this, CreateCake.class);
        startActivity(homepage);
    }
}
