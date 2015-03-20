package com.cs246.bakery.myapplication.model;

import org.json.JSONObject;

/**
 * Created by ricardo on 3/19/2015.
 */
public class CakeType {
    public int id;
    public String name;
    public String description;
    public String image;

    /**
     * Parse json string to CakeType obj
     * @param json
     * @return
     */
    public CakeType parseJson(String json) {
        CakeType cakeType = null;
        try {
            cakeType = new CakeType();
            JSONObject respObj = new JSONObject(json);
            cakeType.id = respObj.getInt("id");
            cakeType.name = respObj.getString("name");
            cakeType.description = respObj.getString("description");
            cakeType.image = respObj.getString("image");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return cakeType;
    }
}
