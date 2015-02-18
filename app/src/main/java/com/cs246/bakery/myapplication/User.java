package com.cs246.bakery.myapplication;

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
    /**
     * User profile (A Admin - U Regular user)
     */
    public char profile;

    public boolean authenticateUser(String username, String password) {
        // Connect to the database to search for user
        return true;
    }
}
