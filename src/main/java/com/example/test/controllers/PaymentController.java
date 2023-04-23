package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Customer;
import com.example.test.entities.Order;
import com.example.test.enums.OrderState;

import com.example.test.interfaces.ItemsObserver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        GlobalEntities.ORDER = new Order(0, OrderState.booked, customer.getTotalPrice(), customer.getId());
        GlobalEntities.ORDER.setItems(customer.getShoppingItems());

        this.loadPaymentPage();
    }
    public void payButtonOnAction() throws IOException
    {
        GlobalEntities.ORDER = new Order(0, OrderState.paid, customer.getTotalPrice(), customer.getId());
        GlobalEntities.ORDER.setItems(customer.getShoppingItems());

        this.loadPaymentPage();
    }

    private void loadPaymentPage() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.PAYMENTPAGE).toURI().toURL());
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 600, 450));
        stage.setTitle("Payment");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.centerOnScreen();
    }
}
