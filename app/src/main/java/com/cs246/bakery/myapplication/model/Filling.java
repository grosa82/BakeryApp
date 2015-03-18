package com.cs246.bakery.myapplication.model;

import com.cs246.bakery.myapplication.model.Characteristic;

/**
 * Created by Bit on 3/2/2015.
 */
public class Filling implements Characteristic {
    @Override
    public void addCharacteristic() {

    }

    @Override
    public void removeCharacteristic() {

    }

    /** Unique ID for each filling */
    public int id;
    /** Name of the filling */
    public String name;
    /** description of filling */
    public String description;
    /** Active or not active */
    public boolean active;
}
