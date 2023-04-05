package com.example.test.entities;

import com.example.test.entities.Item;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Shop
{
    private double id;
    private String name;
    private List<Item> items;
    private String imageSource;
    private Color textColor;
    private double vendor;

    public Shop()
    {
        id = 0;
        name = "";
        List<Item> items = new ArrayList<>();
        imageSource = "";
        textColor = Color.web("#FFFFFF");
        double vendor = 0;
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
    public void AddItem(Item item)
    {
        items.add(item);
    }
    public void DeleteItem(double id) { }
    public void ChangeItem(double id) { }
}
