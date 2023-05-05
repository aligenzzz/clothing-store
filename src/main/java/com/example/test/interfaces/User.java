package com.example.test.interfaces;

import com.example.test.enums.AccessType;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public class User
{
    // uberu public potom
    @SerializedName("id")
    public double id;

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("email")
    public String email;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("accessType")
    public AccessType accessType;

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

    public User(@NotNull User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.accessType = user.getAccessType();
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
