package com.example.test.entities;

import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.User;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User
{
    private List<Item> favouriteItems;
    private List<Item> shoppingItems;
    private List<Order> orders;
    private List<Item> purchasedItems;
    private List<Shop> favouriteShops;

    public Customer() { super(); }
    public Customer(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);

        this.favouriteShops = new ArrayList<>();
        this.shoppingItems = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.purchasedItems = new ArrayList<>();
        this.favouriteShops = new ArrayList<>();
    }

    public List<Item> getFavouriteItems() { return this.favouriteItems; }
    public void setFavouriteItems(List<Item> itemList) { this.favouriteItems = itemList; }
    public List<Item> getShoppingItems() { return this.shoppingItems;}
    public void setShoppingItems(List<Item> itemList) { this.shoppingItems = itemList; }
    public List<Item> getPurchasedItems() { return this.purchasedItems; }
    public void setPurchasedItems(List<Item> itemList) { this.purchasedItems = itemList; }
    public List<Shop> getFavouriteShops() { return this.favouriteShops; }
    public void setFavouriteShops(List<Shop> shopList) { this.favouriteShops = shopList; }

    public double getTotalPrice()
    {
        double result = 0;
        if (this.shoppingItems == null || this.shoppingItems.size() == 0) return result;
        for (Item item: this.shoppingItems) result += item.getPrice();

        return result;
    }

    public void addOrder(Order order) { this.orders.add(order); }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public List<Order> getOrders() { return this.orders; }


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
