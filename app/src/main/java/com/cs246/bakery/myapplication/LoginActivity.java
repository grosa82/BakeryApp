package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Response;
import com.cs246.bakery.myapplication.model.User;


public class LoginActivity extends ActionBarActivity {
    private Helper helper = new Helper(this);
    private ProgressBar progressBar;

    public void onStart() {
        super.onStart();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((EditText) findViewById(R.id.email)).requestFocus();
    }

    public class AuthenticateTask extends AsyncTask<Void, String, User> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected User doInBackground(Void... nullValue) {
            String email = ((EditText) findViewById(R.id.email)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();

            User user = new User(LoginActivity.this);
            return user.authenticate(email, password);
        }

        @Override
        protected void onPostExecute(User response) {
            progressBar.setVisibility(View.GONE);
            if (response != null) {
                Intent homepage = new Intent(LoginActivity.this, MyCakes.class);
                startActivity(homepage);
            } else
                helper.displayMessage("Wrong email or password. Please try again");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signOn(View view) {

        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        Response emailResponse = helper.validateEmail(email);
        Response passwordResponse = helper.validatePassword(password, password);

        if (!emailResponse.success) {
            helper.displayMessage(emailResponse.message);
        } else if (!passwordResponse.success) {
            helper.displayMessage(passwordResponse.message);
        } else {
            new AuthenticateTask().execute();
        }
    }
}
