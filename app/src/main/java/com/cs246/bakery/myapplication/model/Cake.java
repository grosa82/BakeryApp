package com.cs246.bakery.myapplication.model;

import android.app.Activity;
import android.text.TextUtils;

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
    public List<Item> items;

    /**
     * Cake action enumerator
     */
    public enum cakeAction {
        submit,
        cancel,
        approvePrice,
        edit
    }

    public Cake(Activity activity) {
        categories = new ArrayList<Category>();
        items = new ArrayList<Item>();
        helper = new Helper(activity);
    }

    private Cake() {
        categories = new ArrayList<Category>();
        items = new ArrayList<Item>();
    }

    /**
     * Gets the user's cakes
     * @return List of cakes
     */
    public List<Cake> getCakes() {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetCakes");
        requestPackage.setParam("userId", helper.getPreferences("id"));
        requestPackage.setParam("userToken", helper.getPreferences("token"));

        List<Cake> orders = new ArrayList<>();

        String jsonString = helper.callWebService(requestPackage).toString();
        if (jsonString == null || jsonString.equals("null\n") || jsonString.equals("[]\n"))
            return null;
        else {
            try {
                JSONArray orderJson = new JSONArray(jsonString);
                for (int i = 0; i < orderJson.length(); i++) {
                    Cake order = parseCake(orderJson.get(i).toString());
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
     * Gets a user cake
     * @return Cake
     */
    public Cake getCake(String cakeId) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetCake");
        requestPackage.setParam("userId", helper.getPreferences("id"));
        requestPackage.setParam("userToken", helper.getPreferences("token"));
        requestPackage.setParam("cakeId", cakeId);

        Cake cake = null;

        String jsonString = helper.callWebService(requestPackage).toString();
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            try {
                cake = parseCake(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return cake;
        }
    }

    /**
     * Parse a json string to a cake object
     * @param text
     * @return Cake
     */
    private Cake parseCake(String text) {
        Cake cake = new Cake();
        if (text.isEmpty()) {
            return null;
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
                cake.type = new CakeType((Activity)helper.getContext()).parseJson(respObj.getString("type"));
                cake.cakeEvent = respObj.getString("cakeEvent");
                cake.orderDate = helper.parseDate(respObj.getString("orderDate"));
                cake.submitted = respObj.getBoolean("submitted");
                cake.name = respObj.getString("name");
                JSONArray items = respObj.getJSONArray("characteristics");
                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject jsonItem = items.getJSONObject(i);
                        Item item = new Item();
                        item.id = jsonItem.getInt("id");
                        item.name = jsonItem.getString("name");
                        item.description = jsonItem.getString("description");
                        cake.items.add(item);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return cake;
    }

    /**
     * Adds a new cake to the user account
     * @param ageRange
     * @param colors
     * @param writing
     * @param comments
     * @param idCakeType
     * @param items Items IDs that defines the characteristics of the cake
     * @param cakeEvent
     * @param orderName Name to distinguish one order to another
     * @return Response object
     */
    public Response createCake(String ageRange, String colors, String writing, String comments, String idCakeType,
                               List<String> items, String cakeEvent, String orderName) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("CreateCake");
        requestPackage.setParam("ageRange", ageRange);
        requestPackage.setParam("colors", colors);
        requestPackage.setParam("writing", writing);
        requestPackage.setParam("comments", comments);
        requestPackage.setParam("idCakeType", idCakeType);
        requestPackage.setParam("items", TextUtils.join(",", items));
        requestPackage.setParam("cakeEvent", cakeEvent);
        requestPackage.setParam("name", orderName);
        requestPackage.setParam("userId", helper.getPreferences("id"));
        requestPackage.setParam("userToken", helper.getPreferences("token"));
        return helper.callWebService(requestPackage).toResponse();
    }

    /**
     * Updates the cake info
     * @param cakeId Cake id
     * @param ageRange
     * @param colors
     * @param writing
     * @param comments
     * @param idCakeType
     * @param items Items IDs that defines the characteristics of the cake
     * @param cakeEvent
     * @param orderName Name to distinguish one order to another
     * @return Response object
     */
    public Response updateCake(String cakeId, String ageRange, String colors, String writing, String comments, String idCakeType,
                               List<String> items, String cakeEvent, String orderName) {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("UpdateCake");
        requestPackage.setParam("id", cakeId);
        requestPackage.setParam("ageRange", ageRange);
        requestPackage.setParam("colors", colors);
        requestPackage.setParam("writing", writing);
        requestPackage.setParam("comments", comments);
        requestPackage.setParam("idCakeType", idCakeType);
        requestPackage.setParam("items", TextUtils.join(",", items));
        requestPackage.setParam("cakeEvent", cakeEvent);
        requestPackage.setParam("name", orderName);
        requestPackage.setParam("userId", helper.getPreferences("id"));
        requestPackage.setParam("userToken", helper.getPreferences("token"));
        return helper.callWebService(requestPackage).toResponse();
    }

    /**
     * Updates cake actions (submit, cancel, approve price, or enable edit)
     * @param cakeId Cake id
     * @param action
     * @return Response object
     */
    public Response updateCake(String cakeId, cakeAction action) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri("UpdateCake");
        requestPackage.setParam("id", cakeId);
        switch (action) {
            case submit:
                requestPackage.setParam("action", "submit");
                break;
            case cancel:
                requestPackage.setParam("action", "cancel");
                break;
            case approvePrice:
                requestPackage.setParam("action", "approve");
                break;
            case edit:
                requestPackage.setParam("action", "edit");
                break;
        }
        requestPackage.setParam("idUser", helper.getPreferences("id"));
        requestPackage.setParam("token", helper.getPreferences("token"));
        return helper.callWebService(requestPackage).toResponse();
    }
}