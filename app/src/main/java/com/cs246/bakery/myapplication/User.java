package com.cs246.bakery.myapplication;

//import org.ksoap2.serialization.KvmSerializable;
//import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * User class
 */
public class User {
    /**
     * Unique key
     */
    public int id;
    /**
     * Password
     */
    public String password;
    /**
     * Email address
     */
    public String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * User full name
     */
    public String name;
    /**
     * User phone number
     */
    public String phone;

    public boolean authenticateUser(String username, String password) {
        // Connect to the database to search for user
        return true;
    }

    public boolean addUser(String name, String email, String password, String phone) {
        // call web service to add user
        return true;
    }
}
