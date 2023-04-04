package com.example.test.entities;

import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.User;

import java.util.List;

public class Customer extends User
{
    List<Item> favouritesItems;
    List<Item> shoppingItems;
    List<Order> orders;
    List<Item> purchasedItems;
    List<Shop> favouritesShops;

    public Customer(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.accessType = accessType;
    }

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
