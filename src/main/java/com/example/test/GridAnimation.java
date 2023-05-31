package com.example.test;

import com.example.test.controllers.*;
import com.example.test.entities.*;
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
    private final List<?> list;
    private int size = 0;
    private int i = 0;
    private int column = 0;
    private int row = 0;
    private final int maxColumn;
    private final IListener listener;
    public GridAnimation(List<?> list, GridPane gridPane, ScrollPane scrollPane, IListener listener, int maxColumn)
    {
        this.list = list;
        if (list != null) this.size = list.size();

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

        if (this.list == null || this.list.size() == 0) return;

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
        String path = null;

        Item item = null;
        Shop shop = null;
        Order order = null;
        OrderItem orderItem = null;
        Request request = null;
        if (list.get(0) instanceof Item)
        {
            item = (Item) list.get(i);
            path = Constants.ITEM;
        }
        else if (list.get(0) instanceof Shop)
        {
            shop = (Shop) list.get(i);
            path = Constants.SHOP;
        }
        else if (list.get(0) instanceof Order)
        {
            order = (Order) list.get(i);
            path = Constants.ORDER;
        }
        else if (list.get(0) instanceof OrderItem)
        {
            orderItem = (OrderItem) list.get(i);
            path = Constants.ORDERITEM;
        }
        else if (list.get(0) instanceof Request)
        {
            request = (Request) list.get(i);
            path = Constants.REQUEST;
        }

        assert path != null;
        fxmlLoader.setLocation(new File(path).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();

        if (column == maxColumn)
        {
            column = 0;
            row++;
        }

        if (item != null)
        {
            ItemController itemController = fxmlLoader.getController();
            itemController.setData(item, listener);
        }
        else if (shop != null)
        {
            ShopController shopController = fxmlLoader.getController();
            shopController.setData(shop);
        }
        else if (order != null)
        {
            OrderController orderController = fxmlLoader.getController();
            orderController.setData(order);
        }
        else if (orderItem != null)
        {
            OrderItemController orderItemController = fxmlLoader.getController();
            orderItemController.setData(orderItem);
        }
        else if (request != null)
        {
            RequestController requestController = fxmlLoader.getController();
            requestController.setData(request);
        }

        gridPane.add(anchorPane, column++, row);

        GridPane.setMargin(anchorPane, new Insets(10));

        i++;
        if (i >= size) stop();
    }
}