package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.entities.Item;
import com.example.test.interfaces.IListener;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GridAnimation extends AnimationTimer
{
    private final GridPane gridPane;
    private final ScrollPane scrollPane;
    private final List<Item> itemList;
    private int size = 0;
    private int i = 0;
    private int column = 0;
    private int row = 0;
    private final int maxColumn;
    private final IListener listener;
    public GridAnimation(List<Item> itemList, GridPane gridPane, ScrollPane scrollPane, IListener listener, int maxColumn)
    {
        this.itemList = itemList;
        if (itemList != null) this.size = itemList.size();

        this.gridPane = gridPane;
        this.scrollPane = scrollPane;
        this.listener = listener;
        this.maxColumn = maxColumn;

    }
    @Override
    public void handle(long now)
    {
        scrollPane.setHvalue(0);
        scrollPane.setVvalue(0);

        if (this.itemList == null) return;

        try { doHandle(); }
        catch (IOException e) { throw new RuntimeException(e); }

        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);

        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
    }

    private void doHandle() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.ITEM).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();

        Item item = itemList.get(i);
        if (column == maxColumn)
        {
            column = 0;
            row++;
        }

        ItemController itemController = fxmlLoader.getController();
        itemController.setData(item, listener);
        gridPane.add(anchorPane, column++, row);

        GridPane.setMargin(anchorPane, new Insets(10));

        i++;
        if (i >= size) stop();
    }
}