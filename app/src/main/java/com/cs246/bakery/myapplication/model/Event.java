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
    /** Unique ID for each event */
    public int id;
    /** name of the even */
    public String name;
    /** description of the event */
    public String description;
    /** if the event is still active or not */
    public boolean active;
}
