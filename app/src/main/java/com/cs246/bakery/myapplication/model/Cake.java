package com.cs246.bakery.myapplication.model;

import com.cs246.bakery.myapplication.model.Characteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class Cake {
    public Cake() {
        characteristics = new ArrayList<Characteristic>();
    }

    /**
     * Adds a cake
     * @param cake
     */
    void addCake(Cake cake) {

    }

    /**
     * removes a cake
     * @param cake
     */
    void removeCake(Cake cake) {

    }

    /**
     * Loads a cake
     * @param id
     */
    void loadCake(int id) {

    }

    /** Unique ID for each cake */
    public int id;
    /** The array list for the characteristics */
    public List<Characteristic> characteristics;
    /** The range of age of the intended customer */
    public String ageRange;
    /** Writing that will be placed on the cake MAX = 50; */
    public String writing;
    /** Comments for the baker */
    public String comments;
    /** size of the cake in inches */
    public Size size;
    /** Colors on top of the cake */
    public String colors;
    /** Price for the cake */
    public float price;
}
