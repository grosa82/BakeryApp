package com.cs246.bakery.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class PredefinedCake {
    PredefinedCake() {
        characteristics = new ArrayList<Characteristic>();
    }

    /**
     * Adds a new cake
     * @param cake
     */
    void addPredefinedCake(Cake cake) {

    }

    /**
     * Removes a cake
     * @param cake
     */
    void removePredefinedCake(Cake cake) {

    }

    /**
     * Uses a predefined cake to create a new custom cake
     */
    void usePredefinedCake() {

    }

    public int id;
    public List<Characteristic> characteristics;
    public String ageRange;
    public Size size;
    public String colors;
    public boolean active;
    public User createdBy;
}
