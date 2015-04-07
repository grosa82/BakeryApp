package com.cs246.bakery.myapplication.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs246.bakery.myapplication.MainActivity;
import com.cs246.bakery.myapplication.MyCakes;
import com.cs246.bakery.myapplication.R;
import com.cs246.bakery.myapplication.MyProfile;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper class
 */
public class Helper {

    /**
     * Constant with the name of the shared preferences
     */
    private static final String PREFS_NAME = "CakeAppPreferences";
    /**
     * Context
     */
    public Context context;
    private static final String TAG = "Helper";

    public Helper(Activity activity) {
        context = activity;
    }

    private String returnedString;

    public Context getContext() {
        return context;
    }

    /**
     * Display a message on screen
     *
     * @param message message to display
     */
    public void displayMessage(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    /**
     * Display a message on screen
     *
     * @param message message to display
     */
    public void displayFancyMessage(String message) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_toast,
                (ViewGroup) ((Activity) context).findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public String aOrAn(String text) {
        if (startsWithVowel(text))
            return "an";
        else
            return "a";
    }

    private boolean startsWithVowel(String text) {
        String[] vowels = {"a", "e", "i", "o", "u"};
        for (int i = 0; i < 5; i++) {
            if (text.toLowerCase().substring(0, 1) == vowels[i])
                return true;
        }
        return false;
    }

    public AlertDialog displayCompanyInfo() {
        StringBuilder sb = new StringBuilder();
        CompanyInfo info = new CompanyInfo();
        sb.append("Phone: \n" + getPreferences("companyPhone") + "\n\n");
        sb.append("Email: \n" + getPreferences("companyEmail") + "\n\n");
        sb.append("Address: \n" + getPreferences("companyAddress") + "\n\n");
        sb.append("Website: \n" + getPreferences("companyWebsite") + "\n\n");
        return displayOkDialog(sb.toString());
    }

    // Actions

    /**
     * Flags the user indicating he wants to receive a call
     */
    public void callMe() {
        Response result = null;
        new AsyncTask<Void, Void, Response>() {
            @Override
            protected Response doInBackground(Void... params) {
                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod("POST");
                requestPackage.setUri("CallMe");
                requestPackage.setParam("userId", getPreferences("id"));
                requestPackage.setParam("userToken", getPreferences("token"));
                return callWebService(requestPackage).toResponse();
            }

            @Override
            protected void onPostExecute(Response response) {
                if (response.success)
                    displayMessage("Someone will contact you soon");
                else
                    displayMessage("We're sorry but something is not right with our service. Please call us.");
            }
        }.execute(null, null, null);
    }

    /**
     * Redirects the user to the profile screen
     */
    public void goToProfile() {
        Intent intent = new Intent(context, MyProfile.class);
        context.startActivity(intent);
    }

    /**
     * Redirects the user to My Cakes view
     */
    public void goToMyCakes() {
        Intent intent = new Intent(context, MyCakes.class);
        context.startActivity(intent);
    }

    /**
     * Signs out
     */
    public void signOut() {
        deletePreferences();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    // Dialogs

    public AlertDialog displayOkCancelDialog(String message, DialogInterface.OnClickListener okClickListener) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle(getPreferences("companyName"));

        // Add the buttons
        builder.setPositiveButton("Yes", okClickListener);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public AlertDialog displayOkDialog(String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle(getPreferences("companyName"));

        // Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        return dialog;
    }


    /**
     * Save information on SharedPreferences
     *
     * @param key
     * @param value
     */
    public void savePreferences(String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Delete shared preferences
     */
    public void deletePreferences() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        preferences.edit().clear().commit();
    }

    /**
     * Get information from SharedPreferences
     *
     * @param key
     * @return value
     */
    public String getPreferences(String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, "").toString();
    }

    /**
     * Register device Id
     */
    public void registerDeviceId() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                User user = new User((Activity) context);
                user.registerDeviceId();
                return null;
            }
        }.execute(null, null, null);
    }

    /**
     * Parses text
     * @param text
     * @return text or symbol "-" if it was empty
     */
    public String parseText(String text) {
        if (text.isEmpty())
            return " - ";
        else
            return text;
    }

    /**
     * Parse json string response to an object Response
     *
     * @param text json string
     * @return Response object
     */
    private Response parseResponse(String text) {
        Response response = new Response();
        if (text.isEmpty()) {
            response.success = false;
            response.message = "Returned Null";
        } else {
            try {
                JSONObject respObj = new JSONObject(text);
                response.success = respObj.getBoolean("success");
                response.message = respObj.getString("message");
                response.exception = respObj.getString("exception");
                response.createdId = respObj.getInt("id");
            } catch (Exception e) {
                Log.e(TAG, "parseResponse", e);
                return null;
            }
        }
        return response;
    }


    /**
     * Parse a json date to a Date object
     *
     * @param text
     * @return Date
     */
    public Date parseDate(String text) {
        /* i.e. 2015-02-28T00:29:34 */
        String[] dateTxt = text.split("T");
        int year = Integer.parseInt(dateTxt[0].substring(0, 4));
        int month = Integer.parseInt(dateTxt[0].substring(5, 7));
        int day = Integer.parseInt(dateTxt[0].substring(8, 10));
        int hour = Integer.parseInt(dateTxt[1].substring(0, 2));
        int minute = Integer.parseInt(dateTxt[1].substring(3, 5));
        int second = Integer.parseInt(dateTxt[1].substring(6, 8));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return calendar.getTime();
    }

    /**
     * Parse boolean
     *
     * @param text
     * @return
     */
    public Boolean parseBool(String text) {
        return text.equals("Y");
    }

    /**
     * Make a request to the web service layer
     *
     * @param requestPackage
     * @return string returned from server
     */
    public Helper callWebService(RequestPackage requestPackage) {

        //Log.i(this.getClass().getName(), requestPackage.getUri());

        BufferedReader reader = null;
        //String uri = "http://192.168.10.3:8081/api/webapi/" + requestPackage.getUri();
        String uri = "http://cakeapp.toughland.com/api/webapi/" + requestPackage.getUri();
        if (requestPackage.getMethod().equals("GET")) {
            uri += "?" + requestPackage.getEncodedParams() + "&json=true";
        }

        try {
            URL url = new URL(uri);
            if (requestPackage.getMethod().equals("POST"))
                url = new URL(uri + "?json=true");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestPackage.getMethod());

            Log.i(TAG, requestPackage.getMethod() + " " + url.toString());

            if (requestPackage.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(requestPackage.getEncodedParams());
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + '\n');
            }
            returnedString = sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "callWebService", e);
            returnedString = "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "callWebService", e);
                    returnedString = "";
                }
            }
            return this;
        }
    }

    public Helper sendPictureToWebService(final FileInputStream fileInputStream, final String fileName, String cakeId) {

        BufferedReader reader = null;
        String uri = "http://192.168.10.3:8081/api/webapi/SendFile/?json=true";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);

            // creating header
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");

            DataOutputStream dos = new DataOutputStream(con.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("CakePicture");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("PictureFromCake");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // end headers

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            // receives the return string
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + '\n');
            }
            returnedString = sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "callWebService", e);
            returnedString = "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "callWebService", e);
                    returnedString = "";
                }
            }
            return this;
        }
    }

    /**
     * Converts a string to a Response object
     *
     * @return Response
     */
    public Response toResponse() {
        if (returnedString.isEmpty()) {
            Response response = new Response();
            response.message = "Blank";
            response.success = false;
            return response;
        }
        return parseResponse(returnedString);
    }

    @Override
    public String toString() {
        return returnedString;
    }

    /**
     * Validate a phone number
     *
     * @param phone
     * @return
     */
    public Response validatePhone(String phone) {

        Response response = new Response();
        response.success = true;

        // Matches 800-555-5555 | 333-444-5555 | 212-666-1234
        // Non-Matches 000-000-0000 | 123-456-7890 | 2126661234
        String regexForPhone = "^[2-9]\\d{2}-\\d{3}-\\d{4}$";

        if (!phone.matches(regexForPhone)) {
            response.success = false;
            response.message = "Phone should be in the form of ###-###-####";
        }
        return response;
    }

    /**
     * Validate if the text is filled out
     *
     * @param text Text to validate
     * @param name Field name
     * @return Response
     */
    public Response validateRequiredText(String text, String name) {
        Response response = new Response();
        response.success = true;

        if (text.isEmpty()) {
            response.success = false;
            response.message = name + " is required";
        }
        return response;
    }

    /**
     * Validate the name
     *
     * @param name
     * @return
     */
    public Response validateName(String name) {

        Response response = new Response();
        response.success = true;

        // Matches T.F. Johnson | John O'Neil | Mary-Kate Johnson
        // Non-Matches sam_johnson | Joe--Bob Jones | dfjsd0rd
        String regexForName = "^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])?[a-zA-Z]*)*$";

        if (!name.matches(regexForName) && name.length() < 3) {
            response.success = false;
            response.message = "Invalid name. Please try again";
        }
        return response;
    }

    /**
     * Validate email address
     *
     * @param email
     * @return
     */
    public Response validateEmail(String email) {

        Response response = new Response();
        response.success = true;

        // Matches john-smith@example.com | john.smith@example.com | john_smith@x-ample.com
        // Non-Matches .john-smith@example.com | @example.com | johnsmith@example.
        String regexForEmail = "^[0-9a-zA-Z]+([0-9a-zA-Z]*[-._+])*[0-9a-zA-Z]+@" +
                "[0-9a-zA-Z]+([-.][0-9a-zA-Z]+)*([0-9a-zA-Z]*[.])[a-zA-Z]{2,6}$";

        if (!email.matches(regexForEmail)) {
            response.success = false;
            response.message = "Invalid email. Please try again";
        }
        return response;
    }

    /**
     * Validate password
     *
     * @param password
     * @param confirmation
     * @return
     */
    public Response validatePassword(String password, String confirmation) {

        Response response = new Response();
        response.success = true;

        if (!password.equals(confirmation)) {
            response.success = false;
            response.message = "Password and confirmation does not match";
        } else if (password.length() < 6) {
            response.success = false;
            response.message = "Password should be at least 6 characters long";
        }
        return response;
    }
}
