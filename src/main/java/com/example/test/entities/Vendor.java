package com.example.test.entities;

import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.User;

public class Vendor extends User
{
    public Vendor(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.accessType = accessType;
    }

    void ChangeOrderState(double order, OrderState state)
    {

    }

    void RequestHelp(Request request)
    {

    }
}
