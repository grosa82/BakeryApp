package com.cs246.bakery.myapplication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class Order {
    /**
     * Creates an array list for each cake when created.
     */
    public Order() {
        cake = new Cake();
    }
    /** Unique ID for each cake */
    public int id;
    /** Date at which the user submitted the order */
    public Date orderDate;
    /** nickname for order (e.g. son's birthday) */
    public String nickName;
    /** The cake in the order */
    public Cake cake;
    /** If the cake was submitted or not */
    public boolean submitted;
}
