package com.cs246.bakery.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.CakeType;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Response;
import com.cs246.bakery.myapplication.model.User;

import java.util.List;


public class MyProfile extends ActionBarActivity {

    private Helper helper = new Helper(this);
    ProgressBar progressBar;
    EditText name, phone, email, password, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // get the user info
        ((EditText) findViewById(R.id.name)).setText(helper.getPreferences("name"));
        ((EditText) findViewById(R.id.phone)).setText(helper.getPreferences("phone"));
        ((EditText) findViewById(R.id.email)).setText(helper.getPreferences("email"));

        // gets the progress bar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // get the elements
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirm = (EditText)findViewById(R.id.confirm);
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

    public void save(View view) {

        // validate the form
        Response nameResponse = helper.validateName(name.getText().toString());
        Response phoneResponse = helper.validatePhone(phone.getText().toString());
        Response emailResponse = helper.validateEmail(email.getText().toString());
        Response passwordResponse = helper.validatePassword(password.getText().toString(), confirm.getText().toString());

        if (!nameResponse.success) {
            name.requestFocus();
            helper.displayMessage(nameResponse.message);
        } else if (!phoneResponse.success) {
            phone.requestFocus();
            helper.displayMessage(phoneResponse.message);
        } else if (!emailResponse.success) {
            email.requestFocus();
            helper.displayMessage(emailResponse.message);
        } else if (!password.getText().toString().isEmpty() && !passwordResponse.success) {
            password.setText("");
            confirm.setText("");
            password.requestFocus();
            helper.displayMessage(passwordResponse.message);
        } else {
            new AsyncTask<Void, Void, Response>() {
                @Override
                protected void onPreExecute() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected Response doInBackground(Void... params) {
                    User user = new User(MyProfile.this);
                    user.name = name.getText().toString();
                    user.phone = phone.getText().toString();
                    user.email = email.getText().toString();
                    if (!password.getText().toString().isEmpty()) {
                        user.password = password.getText().toString();
                    }
                    return user.updateAccount(user);
                }

                @Override
                protected void onPostExecute(Response response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.success) {
                        helper.displayMessage("Profile Updated");

                        helper.savePreferences("name", name.getText().toString());
                        helper.savePreferences("phone", phone.getText().toString());
                        helper.savePreferences("email", email.getText().toString());

                        helper.goToMyCakes();
                    } else {
                        helper.displayMessage(response.message);
                    }
                }
            }.execute(null, null, null);
        }
    }
}
