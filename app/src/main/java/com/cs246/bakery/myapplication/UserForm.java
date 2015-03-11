package com.cs246.bakery.myapplication;

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
import com.cs246.bakery.myapplication.model.RequestPackage;
import com.cs246.bakery.myapplication.model.Response;
import com.cs246.bakery.myapplication.model.User;

public class UserForm extends ActionBarActivity {

    public Helper helper = new Helper();
    private static final String TAG_USER = "UserForm";

    class Services extends AsyncTask<User, String, Response> {
        @Override
        protected Response doInBackground(User... users) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("POST");
            requestPackage.setUri("CreateUser");
            requestPackage.setParam("name",users[0].name);
            requestPackage.setParam("phone",users[0].phone);
            requestPackage.setParam("email",users[0].email);
            requestPackage.setParam("password",users[0].password);
            return helper.parseResponse((helper.getData(requestPackage)));
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response.success) {
                helper.showAlert(response.message, UserForm.this.getApplicationContext());
                startActivity(new Intent(UserForm.this, signOn.class));
            }
            else
                helper.showAlert(response.message, UserForm.this.getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_form, menu);
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

    public void addUser(View view) {
        User newUser = new User();
        newUser.name = ((EditText)findViewById(R.id.name)).getText().toString();
        newUser.email = ((EditText)findViewById(R.id.email)).getText().toString();
        newUser.phone = ((EditText)findViewById(R.id.phone)).getText().toString();
        newUser.password = ((EditText)findViewById(R.id.password)).getText().toString();
        String confirm = ((EditText)findViewById(R.id.confirm)).getText().toString();

        Log.i(TAG_USER, "Creating User with name " + newUser.name);
        Log.i(TAG_USER, "Creating User with email " + newUser.email);

        // proceed only when the form is valid
        if (isFormValid(newUser, confirm)) {
            new Services().execute(newUser);
        } else {
           Log.e(TAG_USER, "User input invalid");
        }
    }

    private boolean isFormValid(User newUser, String confirm) {
        /*
        http://www.regexlib.net/
        */

        // Matches 800-555-5555 | 333-444-5555 | 212-666-1234
        // Non-Matches 000-000-0000 | 123-456-7890 | 2126661234
        String regexForPhone = "^[2-9]\\d{2}-\\d{3}-\\d{4}$";

        // Matches T.F. Johnson | John O'Neil | Mary-Kate Johnson
        // Non-Matches sam_johnson | Joe--Bob Jones | dfjsd0rd
        String regexForName = "^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])?[a-zA-Z]*)*$";

        // Matches john-smith@example.com | john.smith@example.com | john_smith@x-ample.com
        // Non-Matches .john-smith@example.com | @example.com | johnsmith@example.
        String regexForEmail = "^[0-9a-zA-Z]+([0-9a-zA-Z]*[-._+])*[0-9a-zA-Z]+@" +
                "[0-9a-zA-Z]+([-.][0-9a-zA-Z]+)*([0-9a-zA-Z]*[.])[a-zA-Z]{2,6}$";

        if (!newUser.name.matches(regexForName) || newUser.name.length() < 3) {
            helper.showAlert("Invalid name. Please try again", UserForm.this.getApplicationContext());
            ((EditText)findViewById(R.id.name)).setText("");
            ((EditText)findViewById(R.id.name)).requestFocus();
            return false;
        }
        else if (!newUser.phone.matches(regexForPhone)) {
            helper.showAlert("Phone should be in the form of ###-###-####", UserForm.this.getApplicationContext());
            ((EditText)findViewById(R.id.phone)).setText("");
            ((EditText)findViewById(R.id.phone)).requestFocus();
            return false;
        }
        else if (!newUser.email.matches(regexForEmail)) {
            helper.showAlert("Invalid email. Please try again", UserForm.this.getApplicationContext());
            ((EditText)findViewById(R.id.email)).setText("");
            ((EditText)findViewById(R.id.email)).requestFocus();
            return false;
        }
        else if (!newUser.password.equals(confirm)) {
            helper.showAlert("Password and confirmation does not match", UserForm.this.getApplicationContext());
            ((EditText)findViewById(R.id.confirm)).setText("");
            ((EditText)findViewById(R.id.password)).setText("");
            ((EditText)findViewById(R.id.password)).requestFocus();
            return false;
        }
        else if (newUser.password.length() < 8) {
            helper.showAlert("Password should be at least 8 characters long", UserForm.this.getApplicationContext());
            ((EditText)findViewById(R.id.confirm)).setText("");
            ((EditText)findViewById(R.id.password)).setText("");
            ((EditText)findViewById(R.id.password)).requestFocus();
            return false;
        }
        return true;
    }
}
