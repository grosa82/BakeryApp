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
