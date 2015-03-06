package com.cs246.bakery.myapplication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class Order {
    public Order() {
        cakes = new ArrayList<>();
    }
    public int id;
    public Date orderDate;
    public String nickName;
    public List<Cake> cakes;
    public boolean submitted;
}
