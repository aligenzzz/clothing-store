package com.example.test.entities;

import com.example.test.enums.OrderState;

import java.util.List;

public class Order
{
    private double id;
    private OrderState state;
    private double price;
    private double customer;
    private List<Item> items;

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
    public void setItems(List<Item> items) { this.items = items; }
    public List<Item> getItems() { return this.items; }
}
