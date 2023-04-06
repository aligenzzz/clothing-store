package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;

import java.util.List;

public class Admin extends User
{
    private DatabaseConnector databaseConnector;
    public List<Request> requests;
    public Admin() { super(); }
    public Admin(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);
        databaseConnector = DatabaseConnector.getInstance();
    }

    // OrdersHandler ordersHandler;

    public void EditRole(double id)
    {

    }

    public boolean ProcessRequest(double request)
    {
        return false;
    }
}
