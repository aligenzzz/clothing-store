package com.example.test.entities;

import com.example.test.enums.OrderState;

public class OrderItem
{
    private double id;
    private double order;
    private Item item;
    private double shop;
    private OrderState state;

    public OrderItem()
    {
        id = -1;
        order = -1;
        item = null;
        shop = -1;
        state = null;
    }
    public OrderItem(double id, double order, Item item, double vendor, OrderState state)
    {
        this.id = id;
        this.order = order;
        this.item = item;
        this.shop = vendor;
        this.state = state;
    }

    public double getId() { return this.id; }
    public void setOrder(double order) { this.order = order; }
    public double getOrder() { return this.order; }
    public void setItem(Item item) { this.item = item; }
    public Item getItem() { return this.item; }
    public void setShop(double shop) { this.shop = shop; }
    public double getShop() { return this.shop; }
    public void setState(OrderState state) { this.state = state; }
    public OrderState getState() { return this.state; }
}
