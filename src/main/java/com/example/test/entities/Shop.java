package com.example.test.entities;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Shop
{
    private double id;
    private final String name;
    private final List<Item> items;
    private final String imageSource;
    private final Color textColor;
    private final double vendor;

    public Shop()
    {
        this.id = 0;
        this.name = "";
        this.items = new ArrayList<>();
        this.imageSource = "";
        this.textColor = Color.web("#FFFFFF");
        this.vendor = 0;
    }

    public Shop(double id, String name, List<Item> items, String imageSource, Color textColor, double vendor)
    {
        this.id = id;
        this.name = name;
        this.items = items;
        this.imageSource = imageSource;
        this.textColor = textColor;
        this.vendor = vendor;
    }
    public void setId(double id) { this.id = id; }
    public double getId() { return id; }
    public String getName() { return name; }
    public String getImageSource() { return imageSource; }
    public Color getTextColor() { return textColor; }
    public List<Item> getItems() { return items; }
    public void addItem(Item item)
    {
        items.add(item);
    }
    public void deleteItem(@NotNull Item item) { items.remove(item); }
    public void editItem(@NotNull Item item)
    {
        for (Item i: items)
            if (i.getId() == item.getId())
            {
                i.setName(item.getName());
                i.setPrice(item.getPrice());
                i.setImageSource(item.getImageSource());
            }
    }
}
