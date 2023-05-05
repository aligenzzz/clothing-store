package com.example.test.entities;

import com.example.test.MyPair;
import com.example.test.enums.OrderState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Order
{
    private double id;
    private OrderState state;
    private double price;
    private double customer;
    private List<MyPair<Item, OrderState>> items;

    public Order(double id, OrderState state, double price, double customer)
    {
        this.id = id;
        this.state = state;
        this.price = price;
        this.customer = customer;
    }

    public void setId(double id) { this.id = id; }
    public double getId() { return this.id; }
    public void setState(OrderState state) { this.state = state; }
    public OrderState getState() { return this.state; }
    public void setPrice(double price) { this.price = price; }
    public double getPrice() { return this.price; }
    public void setCustomer(double customer) { this.customer = customer; }
    public double getCustomer() { return this.customer; }
    public void setItems(@NotNull List<Item> items)
    {
        if (this.items == null)
            this.items = new ArrayList<>();
        for (Item i : items)
            this.items.add(new MyPair<>(i, this.state));
    }
    public void setItems_(List<MyPair<Item, OrderState>> items) { this.items = items; }

    public List<MyPair<Item, OrderState>> getItems_() { return this.items; }
    public List<Item> getItems()
    {
        List<Item> items = new ArrayList<>();
        for (MyPair<Item, OrderState> i : this.items)
            items.add(i.getFirst());
        return items;
    }
}
