package com.example.test.entities;

import com.example.test.entities.Item;
import com.example.test.enums.OrderState;

import java.util.List;

public class Order
{
    public double id;
    public OrderState state;
    public List<Item> items;
    public double price;
    public double customer;
}
