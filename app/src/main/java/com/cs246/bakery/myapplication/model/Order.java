package com.cs246.bakery.myapplication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bit on 3/2/2015.
 */
public class Order {
    Order() {
        cakes = new ArrayList<>();
    }

    public void addOrder(Order order) {

    }

    public void removeOrder(Order order) {

    }

    public void loadOrders(User user) {

    }

    public int id;
    public User user;
    public Date orderDate;
    public String nickName;
    public List<Cake> cakes;
    public boolean submitted;
}
