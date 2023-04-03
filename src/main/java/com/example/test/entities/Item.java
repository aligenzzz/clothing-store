package com.example.test.entities;

public class Item
{
    private String name;
    private String image_source;
    private String price;
    private String shop;

    public Item()
    {
        name = "";
        image_source = "";
        price = "";
        shop = "";
    }
    public Item(String name, String image_source, String price, String shop)
    {
        this.name = name;
        this.image_source = image_source;
        this.price = price;
        this.shop = shop;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageSource() { return image_source; }
    public void setImageSource(String image_source) { this.image_source = image_source; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getShop() { return shop; }
    public void setShop(String shop) { this.shop = shop; }
}
