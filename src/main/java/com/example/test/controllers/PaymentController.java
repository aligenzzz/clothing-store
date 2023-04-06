package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.Shop;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class PaymentController
{
    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Button bookButton;
    @FXML
    private Button payButton;
    public void setData()
    {
        Customer customer = (Customer) GlobalEntities.USER;
        totalPriceLabel.setText(customer.getTotalPrice() + " $");
        totalItemsLabel.setText(customer.getShoppingItems().size() + " items");
    }
}
