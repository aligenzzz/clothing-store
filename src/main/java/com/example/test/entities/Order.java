package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.enums.OrderState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
    public void setItems(@NotNull List<Item> items, OrderState state)
    {
        for (Item item : items)
            this.items.add(new OrderItem(0, this.id, item, item.getShop(), state));
    }
    public List<OrderItem> getItems() { return this.items; }
    public void changeState(OrderState state) throws IOException
    {
        DatabaseConnector.getInstance().changeOrderState(this.id, state);
        DatabaseConnector.getInstance().changeOrderItemsState(this.id, state);

        this.state = state;
        for (OrderItem i: this.items)
            i.setState(state);
    }
}
