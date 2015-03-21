package com.cs246.bakery.myapplication.model;

/**
 * Created by ricardo on 3/3/2015.
 */
public class Response {
    /** True if the process was executed with success */
    public Boolean success;
    /** Message returned by the server */
    public String message;
    /** Exception returned by the server */
    public String exception;
    /** When adding new information to the database, it will return the id created on this property */
    public int createdId;
}
