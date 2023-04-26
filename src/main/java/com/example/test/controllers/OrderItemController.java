package com.example.test.controllers;

import com.example.test.entities.Item;
import com.example.test.enums.OrderState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

public class OrderItemController
{
    @FXML private Label itemIdLabel;
    @FXML private Label stateLabel;

    public void setData(@NotNull Item item, OrderState state)
    {
        itemIdLabel.setText(String.valueOf(item.getId()));
        stateLabel.setText(String.valueOf(state));
    }
}
