package com.cs246.bakery.myapplication.model;

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

    private Helper helper = new Helper();

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

    public Cake() {
        categories = new ArrayList<Category>();
    }

    /**
     * Gets the user's orders
     * @param context Activity context
     * @return List of orders
     */
    public List<Cake> getOrders(Context context) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetOrders");
        requestPackage.setParam("id", helper.getPreferences("id", context));
        requestPackage.setParam("token", helper.getPreferences("token", context));

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
        Cake cake = new Cake();
        if (text.isEmpty()) {
            cake = null;
        } else {
            try {
                JSONObject respObj = new JSONObject(text);
                cake.id = respObj.getInt("id");
                cake.ageRange = respObj.getString("ageRange");
                cake.colors = respObj.getString("colors");
                cake.writing = respObj.getString("writing");
                cake.comments = respObj.getString("comments");
                cake.price = respObj.getDouble("price");
                cake.status = new Status().parseJson(respObj.getString("status"));
                cake.type = new CakeType().parseJson(respObj.getString("type"));
                cake.cakeEvent = respObj.getString("cakeEvent");
                cake.orderDate = helper.parseDate(respObj.getString("orderDate"));
                cake.submitted = respObj.getBoolean("submitted");
                cake.name = respObj.getString("name");
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return cake;
    }
}
