package com.cs246.bakery.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.RequestPackage;
import com.cs246.bakery.myapplication.model.Response;
import com.cs246.bakery.myapplication.model.User;


public class signOn extends ActionBarActivity {

    Helper helper = new Helper();

    public void onStart() {
        super.onStart();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.welcome)).setTypeface(tf, Typeface.NORMAL);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((EditText)findViewById(R.id.email)).requestFocus();
    }

    public class AuthenticateTask extends AsyncTask<Void, String, Boolean> {

        @Override
        protected Boolean doInBackground(Void... nullValue ) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("POST");
            requestPackage.setUri("Authenticate");
            requestPackage.setParam("email", ((EditText)findViewById(R.id.email)).getText().toString());
            requestPackage.setParam("password", ((EditText)findViewById(R.id.password)).getText().toString());

            String jsonString = helper.getData(requestPackage);
            if (jsonString == null || jsonString.equals("null\n"))
                return false;
            else {
                User user = helper.parseUser(jsonString);
                helper.savePreferences("id", Integer.toString(user.id), signOn.this.getApplicationContext());
                helper.savePreferences("name", user.name, signOn.this.getApplicationContext());
                helper.savePreferences("phone", user.phone, signOn.this.getApplicationContext());
                helper.savePreferences("email", user.email, signOn.this.getApplicationContext());
                helper.savePreferences("token", user.token, signOn.this.getApplicationContext());
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean response) {

            if (response) {
                Intent homepage = new Intent(signOn.this, account_summary.class);
                startActivity(homepage);
            }
            else
                helper.showAlert("Wrong email or password. Please try again", signOn.this.getApplicationContext());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);
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
