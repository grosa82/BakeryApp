package com.cs246.bakery.myapplication.model;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricardo on 3/20/2015.
 */
public class Category {
    public int id;
    public String name;
    public String description;
    public List<Item> items;
    public int maxQuantity;
    private Helper helper;

    public Category(Activity activity) {
        helper = new Helper(activity);
    }

    /**
     * Gets all cake types
     *
     * @return
     */
    public Map<Integer, String> getCategories() {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetCategories");
        requestPackage.setParam("userId", helper.getPreferences("id"));
        requestPackage.setParam("userToken", helper.getPreferences("token"));
        Map<Integer, String> categories = new HashMap<Integer, String>();

        String jsonString = helper.callWebService(requestPackage).toString();
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            try {
                JSONArray categoriesJson = new JSONArray(jsonString);
                for (int i = 0; i < categoriesJson.length(); i++) {
                    Category category = new Category((Activity) helper.context);
                    JSONObject obj = categoriesJson.getJSONObject(i);
                    categories.put(obj.getInt("id"), obj.getString("name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return categories;
        }
    }
}
