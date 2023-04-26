package com.example.test.entities;

import com.example.test.MyPair;
import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.User;

import java.util.ArrayList;
import java.util.List;

public class Vendor extends User
{
    private List<MyPair<Item, OrderState>> orders;
    public Vendor()
    {
        super();
        orders = new ArrayList<>();
    }
    public Vendor(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);
    }

    public List<MyPair<Item, OrderState>> getOrders() { return orders; }
    public List<Item> getItems()
    {
        List<Item> items = new ArrayList<>();
        for (MyPair<Item, OrderState> item : orders)
            items.add(item.getFirst());
        return items;
    }
    public void setOrders(List<MyPair<Item, OrderState>> orders) { this.orders = orders; }
    void ChangeOrderState(double order, OrderState state)
    {

    }

    void RequestHelp(Request request)
    {

    }
}
