package com.cs246.bakery.myapplication.model;

import com.cs246.bakery.myapplication.model.Characteristic;

/**
 * Created by Bit on 3/2/2015.
 */
public class Flavor implements Characteristic {
    @Override
    public void addCharacteristic() {

    }

    @Override
    public void removeCharacteristic() {

    }

    /** Unique ID for each flavor */
    public int id;
    /** Name of the flavor */
    public String name;
    /** description of the flavor */
    public String description;
    /** Active or not */
    public boolean active;
}
