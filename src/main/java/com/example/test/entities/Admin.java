package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;

import java.util.List;

public class Admin extends User
{
    private DatabaseConnector databaseConnector;
    public List<Request> requests;

    // OrdersHandler ordersHandler;

    public Admin(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.accessType = accessType;
    }

    public void EditRole(double id)
    {

    }

    public boolean ProcessRequest(double request)
    {
        return false;
    }
}
