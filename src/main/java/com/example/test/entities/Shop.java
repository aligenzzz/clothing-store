package com.example.test.entities;

import com.example.test.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class Shop
{
    public double id;
    List<Item> items;

    public Shop()
    {
        List<Item> items = new ArrayList<>();
    }

    void AddItem(Item item)
    {
        items.add(item);
    }

    void DeleteItem(double id)
    {

    }

    void ChangeItem(double id)
    {

    }

}
