package com.example.test.entities;

import com.example.test.enums.OrderState;

import java.util.ArrayList;
import java.util.List;

public class Order
{
    private double id;
    private OrderState state;
    private double price;
    private double customer;
    private List<OrderItem> items = new ArrayList<>();

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
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void setItems(List<Item> items, OrderState state)
    {
        for (Item item : items)
            this.items.add(new OrderItem(0, this.id, item, item.getShop(), state));
    }
    public List<OrderItem> getItems() { return this.items; }
    public List<Item> getOnlyItems()
    {
        List<Item> items = new ArrayList<>();
        for (OrderItem i : this.items)
            items.add(i.getItem());
        return items;
    }
}
