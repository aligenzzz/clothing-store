package com.example.test.interfaces;

import com.example.test.enums.AccessType;

public class User
{
    protected double id;
    protected String username;
    protected String password;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected AccessType accessType;

    public User()
    {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.accessType = null;
    }
    public User(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.accessType = accessType;
    }

    public void setId(double id) { this.id = id; }
    public double getId() { return this.id; }
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return this.username; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return this.password; }
    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return this.email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getFirstName() { return this.firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getLastName() { return this.lastName; }
    public void setAccessType(AccessType accessType) { this.accessType = accessType; }
    public AccessType getAccessType() { return this.accessType; }
}
