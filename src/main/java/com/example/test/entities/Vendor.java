package com.example.test.entities;

import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.User;

public class Vendor extends User
{
    public Vendor() { super(); }
    public Vendor(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);
    }
    void ChangeOrderState(double order, OrderState state)
    {

    }

    void RequestHelp(Request request)
    {

    }
}
