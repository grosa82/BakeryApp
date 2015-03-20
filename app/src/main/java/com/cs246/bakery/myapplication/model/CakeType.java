package com.cs246.bakery.myapplication.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 3/19/2015.
 */
public class CakeType {

    private Helper helper = new Helper();

    public int id;
    public String name;
    public String description;
    public String image;

    /**
     * Parses json string to CakeType obj
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

    /**
     * Gets all cake types
     * @return
     */
    public List<CakeType> getCakeTypes() {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetCakeTypes");

        List<CakeType> cakeTypes = new ArrayList<>();

        String jsonString = helper.getData(requestPackage);
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            try {
                JSONArray cakeTypesJson = new JSONArray(jsonString);
                for (int i = 0; i < cakeTypesJson.length(); i++) {
                    CakeType cakeType = parseJson(cakeTypesJson.get(i).toString());
                    cakeTypes.add(cakeType);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return cakeTypes;
        }
    }

    @Override
    public String toString () {
        return name;
    }
}