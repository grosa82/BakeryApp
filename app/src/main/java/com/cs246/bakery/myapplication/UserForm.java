package com.cs246.bakery.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserForm extends ActionBarActivity {

    public final String SOAP_ACTION = "http://tempuri.org/addUser";
    public  final String OPERATION_NAME = "addUser";
    public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public  final String SOAP_ADDRESS = "http://ubrainy.com/Services.asmx";

    class Services extends AsyncTask<User, String, Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            //invokeWS(new RequestParams());

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
            /*PropertyInfo p = new PropertyInfo();
            p.setName("user");
            p.setValue(users[0]);
            p.setType(User.class);
            request.addProperty(p);*/
            request.addProperty("name", users[0].name);
            request.addProperty("email", users[0].email);
            request.addProperty("phone", users[0].phone);
            request.addProperty("password", users[0].password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_ADDRESS);
            try{
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                //List<User> list = new ArrayList<>();
                /*for (int i =0; i < response.getPropertyCount();i++) {
                    User newUser = new User();
                    newUser.name = ((SoapObject)response.getProperty(0)).getProperty("name").toString();
                    list.add(newUser);
                }*/
                return Boolean.valueOf(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (response)
                showAlert("Account created");
            else
                showAlert("Error creating account");
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

        // proceed only when the form is valid
        if (isFormValid(newUser, confirm)) {
            new Services().execute(newUser);
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
            showAlert("Invalid name. Please try again");
            ((EditText)findViewById(R.id.name)).setText("");
            ((EditText)findViewById(R.id.name)).requestFocus();
            return false;
        }
        else if (!newUser.phone.matches(regexForPhone)) {
            showAlert("Phone should be in the form of ###-###-####");
            ((EditText)findViewById(R.id.phone)).setText("");
            ((EditText)findViewById(R.id.phone)).requestFocus();
            return false;
        }
        else if (!newUser.email.matches(regexForEmail)) {
            showAlert("Invalid email. Please try again");
            ((EditText)findViewById(R.id.email)).setText("");
            ((EditText)findViewById(R.id.email)).requestFocus();
            return false;
        }
        else if (!newUser.password.equals(confirm)) {
            showAlert("Password and confirmation does not match");
            ((EditText)findViewById(R.id.confirm)).setText("");
            ((EditText)findViewById(R.id.password)).setText("");
            ((EditText)findViewById(R.id.password)).requestFocus();
            return false;
        }
        else if (newUser.password.length() < 8) {
            showAlert("Password should be at least 8 characters long");
            ((EditText)findViewById(R.id.confirm)).setText("");
            ((EditText)findViewById(R.id.password)).setText("");
            ((EditText)findViewById(R.id.password)).requestFocus();
            return false;
        }

        return true;
    }
    /**
     * Display a temporary message on screen
     * @param message message to display
     */
    private void showAlert(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50); // position
        toast.show();
    }
}
