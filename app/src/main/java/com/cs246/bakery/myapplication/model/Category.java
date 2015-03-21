package com.cs246.bakery.myapplication.model;

import java.util.List;

/**
 * Created by ricardo on 3/20/2015.
 */
public class Category {
    public int id;
    public String name;
    public String description;
    public List<Item> items;
    public int maxQuantity;
}
