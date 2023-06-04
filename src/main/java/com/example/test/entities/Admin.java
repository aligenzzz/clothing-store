package com.example.test.entities;

import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IObserver;
import com.example.test.interfaces.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();;
    private List<Request> requests;
    private List<Order> orders;

    public Admin() { super(); }
    public Admin(double id, String username, String password, String email, String firstname, String lastname, AccessType accessType)
    {
        super(id, username, password, email, firstname, lastname, accessType);
    }
    public Admin(User user) { super(user); }

    static private final List<IObserver> observers = new ArrayList<>();
    static public void addObserver(IObserver observer) { observers.add(observer); }
    private void notifyObservers()
    {
        for (IObserver observer : observers)
            observer.update();
    }

    public List<Order> getOrders() { return this.orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public void finishOrder(Order order)
    {
        this.orders.remove(order);
        this.notifyObservers();
    }
    public List<Request> getRequests() { return this.requests; }
    public void setRequests(List<Request> requests) { this.requests = requests; }
    public boolean editRole(double user, AccessType accessType) throws IOException { return databaseConnector.editRole(user, accessType); }
    public void processRequest(Request request)
    {
        this.requests.remove(request);
        this.notifyObservers();
    }
}
