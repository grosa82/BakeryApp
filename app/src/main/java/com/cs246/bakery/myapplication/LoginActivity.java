package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;
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
        ((EditText)findViewById(R.id.email)).requestFocus();
    }

    public class AuthenticateTask extends AsyncTask<Void, String, User> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected User doInBackground(Void... nullValue ) {
            String email = ((EditText)findViewById(R.id.email)).getText().toString();
            String password = ((EditText)findViewById(R.id.password)).getText().toString();

            User user = new User(LoginActivity.this);
            return user.authenticate(email, password);
        }

        @Override
        protected void onPostExecute(User response) {
            progressBar.setVisibility(View.GONE);
            if (response != null) {
                Intent homepage = new Intent(LoginActivity.this, account_summary.class);
                startActivity(homepage);
            }
            else
                helper.showAlert("Wrong email or password. Please try again");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_on, menu);
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

    public void signOn(View view) {
        new AuthenticateTask().execute();
    }
}
