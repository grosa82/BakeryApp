package com.cs246.bakery.myapplication.model;

import org.json.JSONObject;

/**
 * Created by ricardo on 3/20/2015.
 */
public class Item {
    public int id;
    public String name;
    public String description;
    public boolean active;

    public Item parseJson(String json) {
        Item item = null;
        try {
            item = new Item();
            JSONObject respObj = new JSONObject(json);
            item.id = respObj.getInt("id");
            item.name = respObj.getString("name");
            item.description = respObj.getString("description");
            item.active = respObj.getBoolean("active");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return item;
    }
}
