package com.cs246.bakery.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Response;
import com.cs246.bakery.myapplication.model.User;

public class CreateAccount extends ActionBarActivity {
    private Helper helper = new Helper(this);
    private final String TAG = "UserForm";
    private Context context;

    class Services extends AsyncTask<User, String, Response> {
        @Override
        protected Response doInBackground(User... users) {
            User user = new User(CreateAccount.this);
            return user.createAccount(users[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response.success) {
                helper.displayMessage(response.message);
                startActivity(new Intent(CreateAccount.this, LoginActivity.class));
            } else
                helper.displayMessage(response.message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        context = getApplicationContext();
    }

    public void addUser(View view) {
        User newUser = new User(CreateAccount.this);
        newUser.name = ((EditText) findViewById(R.id.name)).getText().toString();
        newUser.email = ((EditText) findViewById(R.id.email)).getText().toString();
        newUser.phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        newUser.password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirm = ((EditText) findViewById(R.id.confirm)).getText().toString();
        newUser.registerDeviceId();

        Log.e(TAG, newUser.regID);
        // proceed only when the form is valid
        if (isFormValid(newUser, confirm)) {
            new Services().execute(newUser);
        }
    }

    private boolean isFormValid(User newUser, String confirm) {
        /*
        Regular expressions came from http://www.regexlib.net/
        */

        Response phoneResponse = helper.validatePhone(newUser.phone);
        Response nameResponse = helper.validateName(newUser.name);
        Response emailResponse = helper.validateEmail(newUser.email);
        Response passwordResponse = helper.validatePassword(newUser.password, confirm);

        if (!nameResponse.success) {
            helper.displayMessage(nameResponse.message);
            ((EditText) findViewById(R.id.name)).requestFocus();
            return false;
        } else if (!phoneResponse.success) {
            helper.displayMessage(phoneResponse.message);
            ((EditText) findViewById(R.id.phone)).requestFocus();
            return false;
        } else if (!emailResponse.success) {
            helper.displayMessage(emailResponse.message);
            ((EditText) findViewById(R.id.email)).requestFocus();
            return false;
        } else if (!passwordResponse.success) {
            helper.displayMessage(passwordResponse.message);
            ((EditText) findViewById(R.id.confirm)).setText("");
            ((EditText) findViewById(R.id.password)).setText("");
            ((EditText) findViewById(R.id.password)).requestFocus();
            return false;
        }
        return true;
    }
}
