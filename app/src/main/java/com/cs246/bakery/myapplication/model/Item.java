package com.cs246.bakery.myapplication.model;

/**
 * Created by ricardo on 3/20/2015.
 */
public class Item {
    public int id;
    public String name;
    public String description;
    public boolean active;

    @Override
    public String toString () {
        return name;
    }
}
