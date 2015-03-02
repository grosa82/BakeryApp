package com.cs246.bakery.myapplication.model;

import com.cs246.bakery.myapplication.model.Characteristic;

/**
 * Created by Bit on 3/2/2015.
 */
public class Event implements Characteristic {
    @Override
    public void addCharacteristic() {

    }

    @Override
    public void removeCharacteristic() {

    }

    public int id;
    public String name;
    public String description;
    public boolean active;
}
