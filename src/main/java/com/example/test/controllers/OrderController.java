package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Admin;
import com.example.test.entities.Order;
import com.example.test.entities.OrderItem;
import com.example.test.enums.AccessType;
import com.example.test.enums.OrderState;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class OrderController
{
    @FXML private Label idLabel;
    @FXML private Label itemsLabel;
    @FXML private Label priceLabel;
    @FXML private Button payButton;
    @FXML private Button approveButton;
    @FXML private Button finishButton;
    @FXML private Label orderStateLabel;
    @FXML private ComboBox<AnchorPane> comboBox;

    private Order order;
    public void setData(@NotNull Order order) throws IOException
    {
        this.order = order;

        idLabel.setText(Constants.ID_FORMAT.format(order.getId()));
        itemsLabel.setText(order.getItems().size() + " items");
        priceLabel.setText(Constants.PRICE_FORMAT.format(order.getPrice()) + " $");

        orderStateLabel.setText(order.getState().toString());

        if (order.getState() == OrderState.paid) payButton.setDisable(true);

        if (GlobalEntities.USER.getAccessType() == AccessType.admin)
        {
            payButton.setVisible(false);
            finishButton.setVisible(true);
        }

        comboBox.setMaxWidth(180);

        boolean allWasSent = true;

        for (OrderItem i : order.getItems())
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(Constants.ORDERITEM).toURI().toURL());
            AnchorPane anchorPane = fxmlLoader.load();
            OrderItemController orderItemController = fxmlLoader.getController();
            orderItemController.setData(i);

            comboBox.getItems().add(anchorPane);

            if (i.getState() != OrderState.sent) allWasSent = false;
        }
        comboBox.setEditable(false);
        comboBox.setCellFactory(param -> new AnchorPaneListCell());

        if (allWasSent)
        {
            orderStateLabel.setText("sent");
            payButton.setVisible(false);
            approveButton.setVisible(true);
        }
        if (order.getState() == OrderState.approved)
            payButton.setVisible(false);
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

    public void approveButtonOnAction()
    {
        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                order.changeState(OrderState.approved);

                return null;
            }
        };
        approveButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void finishButtonOnAction()
    {
        Admin admin = (Admin) GlobalEntities.USER;
        admin.finishOrder(order);
        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                order.changeState(OrderState.finished);
                return null;
            }
        };
        finishButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private static class AnchorPaneListCell extends ListCell<AnchorPane>
    {
        @Override
        protected void updateItem(AnchorPane item, boolean empty)
        {
            if (empty || item == null)
            {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(item);
            }
        }
    }
}
