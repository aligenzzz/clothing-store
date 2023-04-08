package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.Order;
import com.example.test.entities.Vendor;
import com.example.test.enums.OrderState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderController
{
    @FXML private Label idLabel;
    @FXML private Label itemsLabel;
    @FXML private Label priceLabel;
    @FXML private AnchorPane customerAnchorPane;
    @FXML private AnchorPane vendorAnchorPane;
    @FXML private Label orderStateLabel;
    @FXML private ChoiceBox<String> stateChoiceBox;
    public void setData(@NotNull Order order)
    {
        idLabel.setText(String.valueOf(order.getId()));
        itemsLabel.setText(order.getItems().size() + " items");
        priceLabel.setText(Constants.FORMAT.format(order.getPrice()) + " $");

        if (GlobalEntities.USER instanceof Customer)
        {
            customerAnchorPane.setVisible(true);
            orderStateLabel.setText(order.getState().toString());
        }
        else if (GlobalEntities.USER instanceof Vendor)
        {
            vendorAnchorPane.setVisible(true);

            ObservableList<String> choices = FXCollections.observableArrayList();
            choices.add(OrderState.booked.toString());
            choices.add(OrderState.paid.toString());
            choices.add(OrderState.confirmed.toString());
            choices.add(OrderState.collected.toString());
            choices.add(OrderState.sent.toString());
            choices.add(OrderState.approved.toString());
            choices.add(OrderState.finished.toString());
            stateChoiceBox.setItems(choices);
        }
    }
}
