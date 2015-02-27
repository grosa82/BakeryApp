package com.cs246.bakery.myapplication.model;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ricardo on 2/26/2015.
 */
public class Helper {
    /**
     * Display a temporary message on screen
     * @param message message to display
     */
    public void showAlert(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50); // position
        toast.show();
    }
}
