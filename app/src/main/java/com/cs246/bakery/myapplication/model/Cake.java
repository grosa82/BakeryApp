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

    void addCake(Cake cake) {

    }

    void removeCake(Cake cake) {

    }

    void loadCake(int id) {

    }

    public int id;
    public List<Characteristic> characteristics;
    public String ageRange;
    public String writing;
    public String commitments;
    public Size size;
    public String colors;
    public float price;
}
