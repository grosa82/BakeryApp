package com.cs246.bakery.myapplication.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 3/20/2015.
 */
public class Category {
    public int id;
    public String name;
    public String description;
    public List<Item> items;
    public int maxQuant;

    public Category parseJson(String json) {
        Category category = null;
        try {
            category = new Category();
            category.items = new ArrayList<>();
            JSONObject respObj = new JSONObject(json);
            category.id = respObj.getInt("id");
            category.name = respObj.getString("name");
            category.description = respObj.getString("description");
            JSONArray itemsJson = respObj.getJSONArray("items");
            for (int i = 0; i < itemsJson.length(); i++) {
                JSONObject obj = itemsJson.getJSONObject(i);
                Item item = new Item();
                item.id = obj.getInt("id");
                item.name = obj.getString("name");
                item.description = obj.getString("description");
                item.active = obj.getBoolean("active");
                category.items.add(item);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return category;
    }
}
