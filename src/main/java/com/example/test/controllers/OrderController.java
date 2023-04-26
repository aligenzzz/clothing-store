package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.entities.Order;
import com.example.test.entities.Vendor;
import com.example.test.enums.OrderState;
import com.example.test.interfaces.OrderObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderController
{
    @FXML private Label idLabel;
    @FXML private Label itemsLabel;
    @FXML private Label priceLabel;
    @FXML private AnchorPane customerAnchorPane;
    @FXML private Button payButton;
    @FXML private AnchorPane vendorAnchorPane;
    @FXML private Label orderStateLabel;
    @FXML private ChoiceBox<String> stateChoiceBox;
    @FXML private Label expandButton;
    @FXML private VBox vBox;


    private Order order;
    public void setData(@NotNull Order order)
    {
        this.order = order;

        idLabel.setText(String.valueOf(Math.floor(order.getId())));
        itemsLabel.setText(order.getItems().size() + " items");
        priceLabel.setText(Constants.FORMAT.format(order.getPrice()) + " $");

        if (GlobalEntities.USER instanceof Customer)
        {
            customerAnchorPane.setVisible(true);
            orderStateLabel.setText(order.getState().toString());

            if (order.getState() == OrderState.paid) payButton.setDisable(true);
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

    public void payButtonOnAction() throws IOException
    {
        order.setState(OrderState.paid);
        GlobalEntities.ORDER = order;

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

    static private final List<OrderObserver> observers = new ArrayList<>();
    static public void addObserver(OrderObserver observer) { observers.add(observer); }
    private void notifyObservers(int index, boolean add)
    {
        for (OrderObserver observer : observers)
            observer.update(index, add);
    }
    public void expandButtonOnAction() throws IOException
    {
        Customer customer = (Customer) GlobalEntities.USER;
        int n = vBox.getChildren().size();

        if (n == 1 && !GlobalEntities.EXPANDEDORDER)
        {
            List<Item> orderItems = DatabaseConnector.getInstance().getOrderItems(order.getId());

            for (Item i : orderItems)
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(new File(Constants.ORDERITEM).toURI().toURL());
                AnchorPane anchorPane = fxmlLoader.load();
                OrderItemController orderItemController = fxmlLoader.getController();
                orderItemController.setData(i);
                vBox.getChildren().add(anchorPane);
            }

            notifyObservers((int) customer.getOrderIndex(order.getId()), true);

            GlobalEntities.EXPANDEDORDER = true;
        }
        else if (n > 1 && GlobalEntities.EXPANDEDORDER)
        {
            vBox.getChildren().remove(1, n);
            notifyObservers((int) customer.getOrderIndex(order.getId()), false);

            GlobalEntities.EXPANDEDORDER = false;
        }
    }
}
