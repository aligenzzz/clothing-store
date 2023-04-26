package com.example.test.controllers;

import com.example.test.entities.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

public class OrderItemController
{
    @FXML private Label itemIdLabel;

    public void setData(@NotNull Item item)
    {
        itemIdLabel.setText(String.valueOf(item.getId()));
    }
}
