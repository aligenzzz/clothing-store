package com.example.test.entities;

import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;

public class NonUser extends User
{
    public NonUser(AccessType accessType)
    {
        this.id = -1;
        this.username = "NaN";
        this.password = "NaN";
        this.email = "NaN";
        this.firstName = "NaN";
        this.lastName = "NaN";
        this.accessType = accessType;
    }

    public NonUser(User user) { super(user); }
}
