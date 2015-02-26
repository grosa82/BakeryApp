package com.cs246.bakery.myapplication.test;

import com.cs246.bakery.myapplication.User;

import junit.framework.TestCase;

/**
 * Created by ricardo on 2/25/2015.
 */
public class UserTest extends TestCase {
    public void testAddUser(){
        User user = new User();
        boolean success = user.addUser("Test", "Email", "Phone", "");
        assertEquals(false, success);
    }
}
