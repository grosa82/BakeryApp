package com.cs246.bakery.myapplication.model;

import org.json.JSONObject;

/**
 * Created by ricardo on 3/20/2015.
 */
public class CompanyInfo {
    private Helper helper = new Helper(null);

    public String name;
    public String phone;
    public String email;
    public String website;
    public String address;

    /**
     * Gets the company info
     * @return CompanyInfo object
     */
    public CompanyInfo getCompanyInfo() {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("GetCompanyInfo");

        String jsonString = helper.callWebService(requestPackage);
        if (jsonString == null || jsonString.equals("null\n"))
            return null;
        else {
            try {
                JSONObject infoJson = new JSONObject(jsonString);
                name = infoJson.getString("name");
                phone = infoJson.getString("phone");
                email = infoJson.getString("email");
                website = infoJson.getString("website");
                address = infoJson.getString("address");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return this;
        }
    }
}
