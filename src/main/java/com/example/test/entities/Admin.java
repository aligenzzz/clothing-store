package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.interfaces.IUser;

import java.util.List;

public class Admin implements IUser
{
    private DatabaseConnector databaseConnector;
    public List<Request> requests;

    // OrdersHandler ordersHandler;

    public void EditRole(double id)
    {

    }

    public boolean ProcessRequest(double request)
    {
        return false;
    }
}
