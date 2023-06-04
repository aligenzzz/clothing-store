package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.IObserver;
import com.example.test.interfaces.User;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vendor extends User
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
    private List<OrderItem> orders;
    private Shop shop;
    public Vendor()
    {
        super();
        orders = new ArrayList<>();
    }
    public Vendor(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);
    }

    static private final List<IObserver> observers = new ArrayList<>();
    static public void addObserver(IObserver observer) { observers.add(observer); }
    private void notifyObservers()
    {
        for (IObserver observer : observers)
            observer.update();
    }
    public List<OrderItem> getOrders() { return orders; }
    public List<Item> getItems()
    {
        List<Item> items = new ArrayList<>();
        for (OrderItem item : orders)
            items.add(item.getItem());
        return items;
    }
    public void setOrders(List<OrderItem> orders) { this.orders = orders; }
    public Shop getShop() { return this.shop; }
    public void setShop(Shop shop) { this.shop = shop; }
    public void changeOrderState(double orderItem, OrderState state) throws IOException
    {
        databaseConnector.changeOrderItemState(orderItem, state);
    }
    public void editItem(@NotNull Item item)
    {
        shop.editItem(item);
        GlobalEntities.SHOP = shop;

        this.notifyObservers();
    }
    public void addItem(@NotNull Item item)
    {
        shop.addItem(item);
        GlobalEntities.SHOP = shop;

        this.notifyObservers();
    }
    public void deleteItem(@NotNull Item item)
    {
        shop.deleteItem(item);
        GlobalEntities.SHOP = shop;

        this.notifyObservers();
    }
    public Vendor(User user) { super(user); }
}
