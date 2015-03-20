package com.cs246.bakery.myapplication.model;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class Cake {
    private Helper helper;

    /** Unique ID for each cake */
    public int id;
    /** The array list for the characteristics */
    public List<Category> categories;
    /** The range of age of the intended customer */
    public String ageRange;
    /** Writing that will be placed on the cake MAX = 50; */
    public String writing;
    /** Comments for the baker */
    public String comments;
    /** Colors on top of the cake */
    public String colors;
    /** Price for the cake */
    public double price;
    /** Status of the cake */
    public Status status;
    /** Cake type */
    public CakeType type;
    /** Cake event */
    public String cakeEvent;
    /** Order date */
    public Date orderDate;
    /** Submitted */
    public Boolean submitted;
    /** Cake name */
    public String name;

    public Cake(Activity activity) {
        categories = new ArrayList<Category>();
        helper = new Helper(activity);
    }

    /**
     * Gets the user's orders
     * @return List of orders
     */
    public List<Cake> getOrders() {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetOrders");
        requestPackage.setParam("id", helper.getPreferences("id"));
        requestPackage.setParam("token", helper.getPreferences("token"));

        List<Cake> orders = new ArrayList<>();

        String jsonString = helper.getData(requestPackage);
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            try {
                JSONArray orderJson = new JSONArray(jsonString);
                for (int i = 0; i < orderJson.length(); i++) {
                    Cake order = parseOrder(orderJson.get(i).toString());
                    orders.add(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return orders;
        }
    }

    /**
     * Parse a json string to an order object
     * @param text
     * @return Cake
     */
    public Cake parseOrder(String text) {
        if (text.isEmpty()) {
            return null;
        } else {
            try {
                JSONObject respObj = new JSONObject(text);
                id = respObj.getInt("id");
                ageRange = respObj.getString("ageRange");
                colors = respObj.getString("colors");
                writing = respObj.getString("writing");
                comments = respObj.getString("comments");
                price = respObj.getDouble("price");
                status = new Status().parseJson(respObj.getString("status"));
                type = new CakeType().parseJson(respObj.getString("type"));
                cakeEvent = respObj.getString("cakeEvent");
                orderDate = helper.parseDate(respObj.getString("orderDate"));
                submitted = respObj.getBoolean("submitted");
                name = respObj.getString("name");
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return this;
    }
}
