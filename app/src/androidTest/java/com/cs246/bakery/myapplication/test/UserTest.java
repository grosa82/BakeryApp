package com.cs246.bakery.myapplication.test;

import com.cs246.bakery.myapplication.model.User;

import junit.framework.TestCase;

/**
 * Created by ricardo on 2/25/2015.
 */
public class UserTest extends TestCase {
    /**
     * @author Ricardo Goncalves
     */
    public void testAddUser(){
        User user = new User();
        boolean success = user.addUser("Test", "Email", "Phone", "");
        assertEquals(true, success);
    }

    /**
     * @author Ricardo Goncalves
     */
    public void testGetName(){
        User user = new User();
        user.setName("MyName");
        assertEquals("MyName", user.getName());
    }

    // Tested by Daniel Marsden
    public void testRemoveUser(){
        User user = new User();
        user.addUser("Test", "myEmail", "password" , "MyPhone");
        boolean worked = user.removeUser("Test", "myEmail", "password" , "MyPhone");
        assertEquals(true, worked);
    }

    // tested by Daniel Marsden
    public void testGetPassword(){
        User user = new User();
        user.setPassword("password");
        String password = user.getPassword();
        assertEquals("password", password);
    }

    /**
     * @author Kim Bit
     */
    public void testGetEmail(){
        User user = new User();
        user.setEmail("test@test.com");
        assertEquals("test@test.com", user.getEmail());
    }

    /**
     * @author Kim Bit
     */
    public void testGetPhone(){
        User user = new User();
        user.setPhone("888-888-8888");
        assertEquals("888-888-8888", user.getPhone());
    }
}
