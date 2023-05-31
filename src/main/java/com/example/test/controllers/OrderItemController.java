package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.entities.OrderItem;
import com.example.test.entities.Vendor;
import com.example.test.enums.OrderState;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OrderItemController
{
    @FXML private Label itemIdLabel;
    @FXML private Label stateLabel;
    @FXML private AnchorPane customerAnchorPane;
    @FXML private AnchorPane vendorAnchorPane;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Button confirmButton;

    private OrderItem orderItem;

    public void setData(@NotNull OrderItem orderItem)
    {
        this.orderItem = orderItem;

        if (GlobalEntities.USER instanceof Customer)
        {
            customerAnchorPane.setVisible(true);

            stateLabel.setText(String.valueOf(orderItem.getState()));
        }
        else
        {
            vendorAnchorPane.setVisible(true);

            choiceBox.getItems().addAll("confirmed", "collected", "sent");
            choiceBox.setValue(orderItem.getState().toString());
        }

        itemIdLabel.setText(Constants.ID_FORMAT.format(orderItem.getItem().getId()));
    }

    public void confirmButtonOnAction()
    {
        OrderState state = OrderState.valueOf(choiceBox.getValue());

        if (orderItem.getState() == state) return;

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                Vendor vendor = (Vendor) GlobalEntities.USER;
                vendor.changeOrderState(orderItem.getId(), state);

                return null;
            }
        };

        confirmButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
