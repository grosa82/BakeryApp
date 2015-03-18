package com.cs246.bakery.myapplication.model;

import android.content.Context;

import com.cs246.bakery.myapplication.account_summary;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class Order {

    private Helper helper = new Helper();

    /**
     * Default constructor
     */
    public Order() {
        cake = new Cake();
    }
    /** Unique ID for each cake */
    public int id;
    /** Date at which the user submitted the order */
    public Date orderDate;
    /** nickname for order (e.g. son's birthday) */
    public String nickName;
    /** The cake in the order */
    public Cake cake;
    /** If the cake was submitted or not */
    public boolean submitted;

    /**
     * Gets the user's orders
     * @param context Activity context
     * @return List of orders
     */
    public List<Order> getOrders(Context context) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetOrders");
        requestPackage.setParam("id", helper.getPreferences("id", context));
        requestPackage.setParam("token", helper.getPreferences("token", context));

        List<Order> orders = new ArrayList<>();

        String jsonString = helper.getData(requestPackage);
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            try {
                JSONArray orderJson = new JSONArray(jsonString);
                for (int i = 0; i < orderJson.length(); i++) {
                    Order order = helper.parseOrder(orderJson.get(i).toString());
                    orders.add(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return orders;
        }
    }
}
