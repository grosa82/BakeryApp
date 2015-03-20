package com.cs246.bakery.myapplication.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 3/20/2015.
 */
public class Rules {
    private Helper helper = new Helper();

    public Rules(int cake_type_id) {
        loadCategories(cake_type_id);
    }

    public List<Category> categories;

    private void loadCategories(Integer cake_type_id) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetRulesByCakeType");
        requestPackage.setParam("id", cake_type_id.toString());

        categories = new ArrayList<>();

        String jsonString = helper.getData(requestPackage);
        if (jsonString == null || jsonString.equals("null\n"))
            categories = null;
        else {
            try {
                JSONArray rulesJson = new JSONArray(jsonString);
                for (int i = 0; i < rulesJson.length(); i++) {
                    JSONObject obj = rulesJson.getJSONObject(i);
                    Category category = new Category();
                    category.maxQuant = obj.getInt("maxQuant");
                    // parses the categories
                    JSONObject categoryJson = obj.getJSONObject("category");
                    category.id = categoryJson.getInt("id");
                    category.name = categoryJson.getString("name");
                    category.description = categoryJson.getString("description");
                    category.items = new ArrayList<>();
                    // parses the items
                    JSONArray items = categoryJson.getJSONArray("items");
                    for (int index = 0; index < items.length(); index++) {
                        JSONObject itemJson = items.getJSONObject(index);
                        Item item = new Item();
                        item.id = itemJson.getInt("id");
                        item.name = itemJson.getString("name");
                        item.description = itemJson.getString("description");
                        item.active = itemJson.getBoolean("active");
                        category.items.add(item);
                    }
                    // adds to the list of rules
                    categories.add(category);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}