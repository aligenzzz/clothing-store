package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.Order;
import com.example.test.enums.OrderState;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class PaymentController
{
    @FXML private Label totalItemsLabel;
    @FXML private Label totalPriceLabel;
    Customer customer = (Customer) GlobalEntities.USER;
    public void setData()
    {
        if(customer.getShoppingItems() == null || customer.getShoppingItems().size() == 0) return;
        totalPriceLabel.setText(Constants.FORMAT.format(customer.getTotalPrice()) + " $");
        totalItemsLabel.setText(customer.getShoppingItems().size() + " items");
    }

    public void bookButtonOnAction() throws IOException
    {
        Order order = new Order(0, OrderState.booked, customer.getTotalPrice(), customer.getId());
        order.setItems(customer.getShoppingItems());
        customer.addOrder(order);
        customer.deleteAllShoppingItems();
    }
    public void payButtonOnAction() throws IOException
    {
        Order order = new Order(0, OrderState.paid, customer.getTotalPrice(), customer.getId());
        order.setItems(customer.getShoppingItems());
        customer.addOrder(order);
        customer.deleteAllShoppingItems();
    }
}
