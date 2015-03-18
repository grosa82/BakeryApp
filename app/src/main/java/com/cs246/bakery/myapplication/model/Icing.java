package com.cs246.bakery.myapplication.model;

import com.cs246.bakery.myapplication.model.Characteristic;

/**
 * Created by Bit on 3/2/2015.
 */
public class Icing implements Characteristic {
    @Override
    public void addCharacteristic() {

    }

    @Override
    public void removeCharacteristic() {

    }

    /** Unique ID for the icing */
    public int id;
    /** name of the icing */
    public String name;
    /** description of icing */
    public String description;
    /** Active or not */
    public boolean active;
}
