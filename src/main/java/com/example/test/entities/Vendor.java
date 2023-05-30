package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vendor extends User
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
    private List<OrderItem> orders;
    public Vendor()
    {
        super();
        orders = new ArrayList<>();
    }
    public Vendor(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);
    }

    public List<OrderItem> getOrders() { return orders; }
    public List<Item> getItems()
    {
        List<Item> items = new ArrayList<>();
        for (OrderItem item : orders)
            items.add(item.getItem());
        return items;
    }
    public void setOrders(List<OrderItem> orders) { this.orders = orders; }
    public void changeOrderState(double orderItem, OrderState state) throws IOException
    {
        databaseConnector.changeOrderState(orderItem, state);
    }

    void RequestHelp(Request request)
    {

    }

    public Vendor(User user) { super(user); }
}
