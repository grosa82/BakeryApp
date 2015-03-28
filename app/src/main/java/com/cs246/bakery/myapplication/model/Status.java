package com.cs246.bakery.myapplication.model;

import org.json.JSONObject;

/**
 * Created by Bit on 3/2/2015.
 */
public class Status {
    public int id;
    public String name;
    public String description;

    /**
     * Parse json string to status obj
     *
     * @param json
     * @return
     */
    public Status parseJson(String json) {
        Status status = null;
        try {
            status = new Status();
            JSONObject respObj = new JSONObject(json);
            status.id = respObj.getInt("id");
            status.name = respObj.getString("name");
            status.description = respObj.getString("description");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return status;
    }
}
