package com.example.test.entities;

import com.example.test.enums.OrderState;
import com.example.test.interfaces.IUser;

import java.util.List;

public class Customer implements IUser
{
    List<Item> favouritesItems;
    List<Item> shoppingItems;
    List<Order> orders;
    List<Item> purchasedItems;
    List<Shop> favouritesShops;

    void CreateOrder(Order order)
    {
        orders.add(order);
    }

    void CancelOrder(double id)
    {

    }

    void ChangeOrder(double id)
    {

    }

    OrderState TrackOrder(double id)
    {
        return OrderState.booked;
    }

    public void RequestHelp(Request request)
    {

    }
}
