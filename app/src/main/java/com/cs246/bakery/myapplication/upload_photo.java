package com.cs246.bakery.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Response;

import java.io.FileInputStream;


public class upload_photo extends ActionBarActivity {

    private Helper helper = new Helper(this);
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

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

        if (id == R.id.my_cakes) {
            helper.goToMyCakes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadPhoto(View view) {
        new AsyncTask<Void, Void, Response>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(upload_photo.this, "Please wait ...", "Uploading Picture", true);
            }

            @Override
            protected Response doInBackground(Void... params) {
                return helper.sendPictureToWebService(new FileInputStream(""), "fileName").toResponse();
            }

            @Override
            protected void onPostExecute(Response response) {
                progressDialog.dismiss();
                helper.displayMessage(response.message);
            }
        }.execute(null, null, null);
    }
}
