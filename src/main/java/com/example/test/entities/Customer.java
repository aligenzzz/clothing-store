package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IObserver;
import com.example.test.interfaces.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User
{
    static private final List<IObserver> observers = new ArrayList<>();
    static public void addObserver(IObserver observer) { observers.add(observer); }
    private void notifyObservers()
    {
        for (IObserver observer : observers)
            observer.update();
    }
    private List<Item> favouriteItems;
    private List<Item> shoppingItems;
    private List<Order> orders;
    private List<Item> purchasedItems;
    private List<Shop> favouriteShops;

    public Customer() { super(); }
    public Customer(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);

        this.favouriteShops = new ArrayList<>();
        this.shoppingItems = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.purchasedItems = new ArrayList<>();
        this.favouriteShops = new ArrayList<>();
    }

    public Customer(User user) { super(user); }

    public List<Item> getFavouriteItems() { return this.favouriteItems; }
    public void setFavouriteItems(List<Item> itemList) { this.favouriteItems = itemList; }
    public List<Item> getShoppingItems() { return this.shoppingItems;}
    public void setShoppingItems(List<Item> itemList) { this.shoppingItems = itemList; }
    public List<Item> getPurchasedItems() { return this.purchasedItems; }
    public void setPurchasedItems(List<Item> itemList) { this.purchasedItems = itemList; }
    public List<Shop> getFavouriteShops() { return this.favouriteShops; }
    public void setFavouriteShops(List<Shop> shopList) { this.favouriteShops = shopList; }

    public void addFavouriteShop(double shop) throws IOException
    {
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

        if (this.favouriteShops != null)
        {
            this.favouriteShops.add(databaseConnector.getShop(shop));
            this.notifyObservers();
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                databaseConnector.addFavouriteShop(id, shop);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    public void deleteFavouriteShop(double shop) throws IOException
    {
        for (Shop s: this.favouriteShops)
            if (s.getId() == shop)
            {
                this.favouriteShops.remove(s);
                break;
            }
        this.notifyObservers();

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector.getInstance().deleteFavouriteShop(id, shop);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public double getTotalPrice()
    {
        double result = 0;
        if (this.shoppingItems == null || this.shoppingItems.size() == 0) return result;
        for (Item item: this.shoppingItems) result += item.getPrice();

        return result;
    }

    public void addFavouriteItem(double item) throws IOException
    {
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

        if (this.favouriteItems != null)
        {
            this.favouriteItems.add(databaseConnector.getItem(item));
            this.notifyObservers();
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                databaseConnector.addFavouriteItem(id, item);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void addShoppingItem(double item) throws IOException
    {
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

        if (this.shoppingItems != null)
        {
            this.shoppingItems.add(databaseConnector.getItem(item));
            this.notifyObservers();
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                databaseConnector.addShoppingItem(id, item);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteFavouriteItem(double item)
    {
        for (Item i: this.favouriteItems)
            if (i.getId() == item)
            {
                this.favouriteItems.remove(i);
                break;
            }
        this.notifyObservers();

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector.getInstance().deleteFavouriteItem(id, item);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteShoppingItem(double item)
    {
        for (Item i: this.shoppingItems)
            if (i.getId() == item)
            {
                this.shoppingItems.remove(i);
                break;
            }
        this.notifyObservers();

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector.getInstance().deleteShoppingItem(id, item);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteAllShoppingItems() throws IOException
    {
        this.shoppingItems.clear();
        Platform.runLater(this::notifyObservers);

        DatabaseConnector.getInstance().deleteAllShoppingItems(id);
    }

    public void addOrder(Order order) throws IOException
    {
        this.orders.add(order);
        DatabaseConnector.getInstance().addOrder(order);
    }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public List<Order> getOrders() { return this.orders; }
}
