package com.example.test.entities;

public class Item
{
    private double id;
    private String name;
    private String imageSource;
    private double price;
    private double shop;

    public Item()
    {
        name = "";
        imageSource = "";
        price = 0;
        shop = 0;
    }
    public Item(double id, String name, String imageSource, double price, double shop)
    {
        this.id = id;
        this.name = name;
        this.imageSource = imageSource;
        this.price = price;
        this.shop = shop;
    }
    public double getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageSource() { return imageSource; }
    public void setImageSource(String image_source) { this.imageSource = image_source; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getShop() { return shop; }
    public void setShop(double shop) { this.shop = shop; }
}
